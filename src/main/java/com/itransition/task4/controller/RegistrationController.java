package com.itransition.task4.controller;

import com.itransition.task4.model.UserRegistrationForm;
import com.itransition.task4.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sign-up")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("isAnonymous()")
    public String signUpPage() {
        return "/sign_up";
    }

    @PostMapping
    @PreAuthorize("isAnonymous()")
    public String processUserRegistration(UserRegistrationForm registrationForm) {
        if (userService.registerUser(registrationForm) != null) return "redirect:/sign-in";
        else if (userService.getUserByEmail(registrationForm.getEmail()) != null) return "redirect:/sign-up?error=exists";
        else return "redirect:/sign-up?error=unknown";
    }
}
