package com.kanbee.project.controller;

import com.kanbee.project.model.Reservation;
import com.kanbee.project.service.ReservationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        addCommonAttributes(model);
        List<Reservation> reservations = reservationService.getAllReservations();
        
        // 대기 중인 예약 건수 계산
        long pendingCount = reservations.stream()
            .filter(r -> "PENDING".equals(r.getStatus()))
            .count();
            
        model.addAttribute("reservations", reservations);
        model.addAttribute("pendingCount", pendingCount);
        
        return "admin";
    }

    @PostMapping("/reservation/update-status")
    public String updateStatus(@RequestParam("id") Long id, @RequestParam("status") String status) {
        reservationService.updateStatus(id, status);
        return "redirect:/admin";
    }

    @PostMapping("/reservation/update-comment")
    public String updateComment(@RequestParam("id") Long id, @RequestParam("comment") String comment) {
        reservationService.updateAdminComment(id, comment);
        return "redirect:/admin";
    }

    @PostMapping("/reservation/update-ticket-link")
    public String updateTicketLink(@RequestParam("id") Long id, @RequestParam("ticketLink") String ticketLink) {
        reservationService.updateTicketLink(id, ticketLink);
        return "redirect:/admin";
    }

    @PostMapping("/reservation/update")
    public String updateReservation(@ModelAttribute Reservation reservation) {
        Reservation existing = reservationService.getReservationById(reservation.getId());
        if (existing != null) {
            existing.setCategory(reservation.getCategory());
            existing.setRequestTime(reservation.getRequestTime());
            existing.setHeadcount(reservation.getHeadcount());
            existing.setRequirements(reservation.getRequirements());
            existing.setReferenceUrl(reservation.getReferenceUrl());
            existing.setImageUrl(reservation.getImageUrl());
            existing.setTicketLink(reservation.getTicketLink());
            existing.setAdminComment(reservation.getAdminComment());
            existing.setStatus(reservation.getStatus());
            
            reservationService.updateReservation(existing);
        }
        return "redirect:/admin";
    }

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");
        model.addAttribute("isLoggedIn", isLoggedIn);
        if (isLoggedIn) {
            model.addAttribute("userNickname", auth.getName());
            boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        }
    }
}
