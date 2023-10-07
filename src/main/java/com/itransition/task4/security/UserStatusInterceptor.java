package com.itransition.task4.security;

import com.itransition.task4.model.User;
import com.itransition.task4.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class UserStatusInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof User user) {
            User userRefreshedInfo = userService.getUserByEmail(user.getEmail());
            if (userRefreshedInfo == null || userRefreshedInfo.getStatus().equals("Blocked")) {
                return signUserOut(response);
            }
        }
        return true;
    }

    private static boolean signUserOut(HttpServletResponse response) throws IOException {
        response.sendRedirect("/sign-out");
        return false;
    }
}
