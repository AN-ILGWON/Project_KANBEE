package com.kanbee.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class PolicyController {

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");
        model.addAttribute("isLoggedIn", isLoggedIn);
        if (isLoggedIn) {
            model.addAttribute("currentUsername", auth.getName());
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        }
    }

    @GetMapping("/policy/privacy")
    public String privacyPolicy(Model model) {
        addCommonAttributes(model);
        return "policy/privacy";
    }

    @GetMapping("/policy/terms")
    public String termsOfService(Model model) {
        addCommonAttributes(model);
        return "policy/terms";
    }
    
    @GetMapping("/policy/copyright")
    public String copyrightPolicy(Model model) {
        addCommonAttributes(model);
        return "policy/copyright";
    }

    @GetMapping("/policy/email-refusal")
    public String emailRefusal(Model model) {
        addCommonAttributes(model);
        return "policy/email_refusal";
    }

    @GetMapping("/policy/youth-protection")
    public String youthProtection(Model model) {
        addCommonAttributes(model);
        return "policy/youth_protection";
    }
}
