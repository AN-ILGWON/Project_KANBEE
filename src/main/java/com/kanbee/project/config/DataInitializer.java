package com.kanbee.project.config;

import com.kanbee.project.model.Community;
import com.kanbee.project.model.User;
import com.kanbee.project.service.CommunityService;
import com.kanbee.project.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CommunityService communityService;
    private final UserService userService;

    public DataInitializer(CommunityService communityService, UserService userService) {
        this.communityService = communityService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        // 0. 관리자 계정 확인 및 생성/업데이트
        createOrUpdateAdminUser("superadmin", "admin1234**", "最高管理者 Kanbee");

        // 1. 관리자(jsl26db)가 작성한 글 삭제
        try {
            communityService.deletePostsByAuthor("jsl26db");
            System.out.println("Deleted admin posts.");
        } catch (Exception e) {
            System.out.println("Failed to delete admin posts: " + e.getMessage());
        }

        // 2. 추가 유저 생성 (없으면 생성)
        createTestUser("tanaka_yui", "田中 結衣");
        createTestUser("sato_ken", "佐藤 健");
        createTestUser("ito_misaki", "伊藤 美咲");
        createTestUser("yamada_taro", "山田 太郎");
        createTestUser("suzuki_hanako", "鈴木 花子");
        createTestUser("kimura_takuya", "木村 拓哉");
        createTestUser("matsumoto_jun", "松本 潤");
        createTestUser("inoue_mao", "井上 真央");

        // 3. 샘플 데이터 확인 및 추가
        List<Community> posts = communityService.getAllPosts();
        if (posts.size() < 50) {
            System.out.println("Inserting sample community posts...");
            insertSamplePosts();
        }
    }

    private void createOrUpdateAdminUser(String username, String password, String nickname) {
        User admin = userService.findByUsername(username);
        if (admin == null) {
            User newAdmin = new User();
            newAdmin.setUsername(username);
            newAdmin.setPassword(password); // register a new user
            newAdmin.setNickname(nickname);
            newAdmin.setRole("ADMIN");
            userService.register(newAdmin);
            System.out.println("Admin user '" + username + "' created.");
        } else {
            // 비밀번호가 일치하지 않으면 업데이트
            if (!userService.checkPassword(admin, password)) {
                userService.updatePassword(username, password);
                System.out.println("Admin user '" + username + "' password updated.");
            }
        }
    }

    private void createTestUser(String username, String nickname) {
        if (!userService.isUsernameAvailable(username)) {
            return; // 이미 존재함
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword("1234"); // Default password
        user.setNickname(nickname);
        user.setRole("USER");
        userService.register(user);
    }

    private void insertSamplePosts() {
        String[] users = {"tanaka_yui", "sato_ken", "ito_misaki", "yamada_taro", "suzuki_hanako", "kimura_takuya", "matsumoto_jun", "inoue_mao"};
        String[] categories = {"TALK", "TIP", "QUESTION", "INFO", "REVIEW"};
        
        // Image pool
        String[] images = {
            "https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=800&q=80",
            "https://images.unsplash.com/photo-1580651315530-69c8e0026377?auto=format&fit=crop&w=800&q=80",
            "https://images.unsplash.com/photo-1596436889106-be35e843f974?auto=format&fit=crop&w=800&q=80",
            "https://images.unsplash.com/photo-1533929736472-11199a9402a8?auto=format&fit=crop&w=800&q=80",
            "https://images.unsplash.com/photo-1590079081829-1658b1933580?auto=format&fit=crop&w=800&q=80",
            "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&w=800&q=80",
            "https://images.unsplash.com/photo-1538481199705-c710c4e965fc?auto=format&fit=crop&w=800&q=80",
            "https://images.unsplash.com/photo-1583244673822-19e4860d5e16?auto=format&fit=crop&w=800&q=80",
            "https://images.unsplash.com/photo-1634914757360-153202951559?auto=format&fit=crop&w=800&q=80",
            "https://images.unsplash.com/photo-1549885876-068305c48719?auto=format&fit=crop&w=800&q=80"
        };

        Random random = new Random();

        for (String category : categories) {
            for (int i = 0; i < 12; i++) { // Generate 12 posts per category
                Community post = new Community();
                post.setCategory(category);
                post.setAuthor(users[random.nextInt(users.length)]);
                
                // Content based on category
                if (category.equals("REVIEW")) {
                    post.setTitle("ソウルの人気スポットレビュー #" + (i + 1));
                    post.setContent("この場所は本当に素晴らしかったです。雰囲気も良く、サービスも親切でした。ぜひ行ってみてください！おすすめ度は5つ星です。");
                } else if (category.equals("TIP")) {
                    post.setTitle("韓国旅行の便利なヒント #" + (i + 1));
                    post.setContent("韓国では地図アプリはNaver Mapがおすすめです。Google Mapsよりも詳細な情報が得られます。");
                } else if (category.equals("TALK")) {
                    post.setTitle("今日の出来事 #" + (i + 1));
                    post.setContent("今日は天気が良くて散歩日和でした。美味しいコーヒーも飲めて幸せな一日でした。皆さんはどう過ごしましたか？");
                } else if (category.equals("QUESTION")) {
                    post.setTitle("質問があります #" + (i + 1));
                    post.setContent("来週ソウルに行くのですが、おすすめのレストランはありますか？辛いものはあまり得意ではありません。");
                } else if (category.equals("INFO")) {
                    post.setTitle("イベント情報 #" + (i + 1));
                    post.setContent("今週末、漢江公園でフェスティバルが開催されるそうです。屋台もたくさん出るみたいですよ！");
                }
                
                // Add variety
                if (i % 3 == 0) post.setTitle(post.getTitle() + " (おすすめ)");
                if (i % 2 == 0) post.setImageUrl(images[random.nextInt(images.length)]); // 50% chance of image
                
                communityService.savePost(post);
            }
        }
        System.out.println("Inserted sample posts for all categories.");
    }
}
