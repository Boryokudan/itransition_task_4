package com.itransition.task4.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/sign-in")
    @PreAuthorize("isAnonymous()")
    public String getSignInPage() {
        return "/sign_in";
    }
}
