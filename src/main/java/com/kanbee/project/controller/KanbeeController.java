package com.kanbee.project.controller;

import com.kanbee.project.model.Community;
import com.kanbee.project.model.Comment;
import com.kanbee.project.model.Reservation;
import com.kanbee.project.service.CommunityService;
import com.kanbee.project.service.CommentService;
import com.kanbee.project.service.ReservationService;
import com.kanbee.project.service.UserService;
import com.kanbee.project.model.HotPlace;
import com.kanbee.project.service.HotPlaceService;
import com.kanbee.project.service.KanbeeLoggingService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

@Controller
public class KanbeeController {

    @Value("${file.upload.path}")
    private String uploadPath;

    @GetMapping("/reservation/detail/{id}")
    public String reservationDetail(@PathVariable("id") Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        Reservation reservation = reservationService.getReservationById(id);
        if (reservation == null) {
            return "redirect:/mypage";
        }

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Only allow access if user is the owner or an admin
        if (!reservation.getUsername().equals(auth.getName()) && !isAdmin) {
            return "redirect:/mypage";
        }

        model.addAttribute("reservation", reservation);

        // AI 추천 로직 실행 (Gemini AI -> Fallback to Rule-based)
        List<HotPlace> aiRecommendations = aiRecommendationService.getRecommendations(reservation.getCategory());

        if (aiRecommendations.isEmpty()) {
            // AI 호출 실패 시 기존 규칙 기반 로직 사용
            aiRecommendations = hotPlaceService.getAiRecommendations(reservation.getCategory());
            model.addAttribute("aiSource", "KANBEE Database");
        } else {
            model.addAttribute("aiSource", "KANBEE AI (Gemini)");
        }

        model.addAttribute("aiRecommendations", aiRecommendations);

        addCommonAttributes(model);
        return "reservation_detail";
    }

    private final CommunityService communityService;
    private final CommentService commentService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final KanbeeLoggingService loggingService;
    private final HotPlaceService hotPlaceService;
    private final com.kanbee.project.service.AiRecommendationService aiRecommendationService;

    public KanbeeController(CommunityService communityService, CommentService commentService,
            ReservationService reservationService, UserService userService, KanbeeLoggingService loggingService,
            HotPlaceService hotPlaceService,
            com.kanbee.project.service.AiRecommendationService aiRecommendationService) {
        this.communityService = communityService;
        this.commentService = commentService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.loggingService = loggingService;
        this.hotPlaceService = hotPlaceService;
        this.aiRecommendationService = aiRecommendationService;
    }

    @GetMapping("/api/check-username")
    @org.springframework.web.bind.annotation.ResponseBody
    public boolean checkUsername(@org.springframework.web.bind.annotation.RequestParam("username") String username) {
        if (username == null) {
            return false;
        }
        return userService.isUsernameAvailable(username.trim());
    }

    @GetMapping("/")
    public String index(Model model) {
        addCommonAttributes(model);
        List<Community> recentPosts = communityService.getAllPosts();
        if (recentPosts.size() > 5) {
            recentPosts = recentPosts.subList(0, 5);
        }
        model.addAttribute("recentPosts", recentPosts);
        return "index";
    }

    @PostMapping("/reservation/request")
    public String requestReservation(@ModelAttribute Reservation reservation,
            @RequestParam(value = "file", required = false) MultipartFile file,
            RedirectAttributes redirectAttributes) {

        System.out.println("Received Reservation Request: " + reservation);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        // Handle file upload
        if (file != null && !file.isEmpty()) {
            try {
                // Validate file type
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    redirectAttributes.addFlashAttribute("errorMessage", "alert.invalid_file_type");
                    return "redirect:/mypage";
                }

                String fileName = uploadFile(file);
                reservation.setImageUrl("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace(); // Log error
                redirectAttributes.addFlashAttribute("errorMessage", "alert.upload_failed");
                return "redirect:/mypage";
            }
        }

        reservation.setUsername(auth.getName());
        reservationService.requestReservation(reservation);

        // i18n 메시지 키를 사용하여 성공 메시지 전달
        redirectAttributes.addFlashAttribute("successMessage", "alert.reservation_requested");
        return "redirect:/mypage";
    }

    @GetMapping("/community/post/{id}")
    public String communityPost(@PathVariable("id") Long id, Model model) {
        addCommonAttributes(model);
        Community post = communityService.getPostById(id);
        if (post == null) {
            return "redirect:/community";
        }
        model.addAttribute("post", post);
        return "community_detail";
    }

    private String uploadFile(MultipartFile file) throws IOException {
        Path uploadDir = Paths.get(uploadPath);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..")) {
            throw new IOException("Invalid filename");
        }

        // Validate extension
        String extension = "";
        int i = originalFilename.lastIndexOf('.');
        if (i > 0) {
            extension = originalFilename.substring(i + 1).toLowerCase();
        }

        if (!List.of("jpg", "jpeg", "png", "gif").contains(extension)) {
            throw new IOException("Invalid file extension");
        }

        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "_" + originalFilename;

        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    @GetMapping("/about")
    public String about(Model model) {
        addCommonAttributes(model);
        return "about";
    }

    @GetMapping("/community")
    public String community(Model model) {
        addCommonAttributes(model);
        model.addAttribute("posts", communityService.getAllPosts());
        return "community";
    }

    @GetMapping("/login")
    public String login(Model model) {
        addCommonAttributes(model);
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        addCommonAttributes(model);
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute com.kanbee.project.model.User user, RedirectAttributes redirectAttributes) {
        if (userService.register(user)) {
            redirectAttributes.addFlashAttribute("successMessage", "signup.success.registered"); // messages.properties
                                                                                                 // key
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "signup.error.already_exists"); // messages.properties
                                                                                                 // key
            return "redirect:/signup";
        }
    }

    @GetMapping("/mypage")
    public String mypage(Model model) {
        addCommonAttributes(model);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = auth.getName();
            model.addAttribute("userReservations", reservationService.getReservationsByUsername(username));
            model.addAttribute("userPosts", communityService.getPostsByAuthor(username));
        }
        return "mypage";
    }

    @GetMapping("/community/write")
    public String showWriteForm(Model model) {
        addCommonAttributes(model);
        return "community_write";
    }

    @PostMapping("/community/write")
    public String submitWriteForm(@RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") String category,
            @RequestParam(value = "file", required = false) MultipartFile file,
            RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }
        String author = auth.getName();
        Community post = new Community();
        post.setTitle(title);
        post.setContent(content);
        post.setCategory(category);
        post.setAuthor(author);

        // Handle file upload
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = uploadFile(file);
                post.setImageUrl("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            }
        }

        communityService.savePost(post);
        redirectAttributes.addFlashAttribute("successMessage", "게시글이 등록되었습니다.");
        return "redirect:/community";
    }

    @PostMapping("/community/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        Community post = communityService.getPostById(id);
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (post != null && (post.getAuthor().equals(auth.getName()) || isAdmin)) {
            communityService.deletePost(id);
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "삭제 권한이 없습니다.");
        }
        return "redirect:/community";
    }

    @GetMapping("/community/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        addCommonAttributes(model);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        Community post = communityService.getPostById(id);
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (post == null || (!post.getAuthor().equals(auth.getName()) && !isAdmin)) {
            return "redirect:/community";
        }

        model.addAttribute("post", post);
        return "community_edit";
    }

    @PostMapping("/community/edit/{id}")
    public String updatePost(@PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") String category,
            @RequestParam(value = "file", required = false) MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return "redirect:/login";
            }

            Community post = communityService.getPostById(id);
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (post != null && (post.getAuthor().equals(auth.getName()) || isAdmin)) {
                post.setTitle(title);
                post.setContent(content);
                post.setCategory(category);

                // Handle file upload
                if (file != null && !file.isEmpty()) {
                    try {
                        String fileName = uploadFile(file);
                        post.setImageUrl("/uploads/" + fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                        redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
                    }
                }

                communityService.updatePost(post);
                redirectAttributes.addFlashAttribute("successMessage", "게시글이 수정되었습니다.");
                return "redirect:/community/post/" + id;
            }

            return "redirect:/community";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/community/edit/" + id;
        }
    }

    @PostMapping("/community/comment")
    public String addComment(@RequestParam("postId") Long postId,
            @RequestParam("content") String content,
            RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setContent(content);
        comment.setAuthor(auth.getName());

        // Log the new comment
        try {
            String logMsg = String.format("New Comment - Author: %s, PostID: %d",
                    auth.getName(), postId);
            System.out.println("[KANBEE-LOG] " + logMsg);
            loggingService.logActivity(logMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        commentService.addComment(comment);

        return "redirect:/community/post/" + postId;
    }

    @PostMapping("/community/comment/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id, @RequestParam("postId") Long postId,
            RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        Comment comment = commentService.getCommentById(id);
        if (comment == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "댓글을 찾을 수 없습니다.");
            return "redirect:/community/post/" + postId;
        }

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (comment.getAuthor().equals(auth.getName()) || isAdmin) {
            commentService.deleteComment(id);
            redirectAttributes.addFlashAttribute("successMessage", "댓글이 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "삭제 권한이 없습니다.");
        }

        return "redirect:/community/post/" + postId;
    }

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");
        model.addAttribute("isLoggedIn", isLoggedIn);
        if (isLoggedIn) {
            Object principal = auth.getPrincipal();
            if (principal instanceof com.kanbee.project.security.CustomUserDetails) {
                model.addAttribute("userNickname",
                        ((com.kanbee.project.security.CustomUserDetails) principal).getNickname());
            } else {
                model.addAttribute("userNickname", auth.getName());
            }
            model.addAttribute("currentUsername", auth.getName());

            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        }
    }
}