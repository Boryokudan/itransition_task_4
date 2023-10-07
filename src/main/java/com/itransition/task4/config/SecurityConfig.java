package com.itransition.task4.config;

import com.itransition.task4.model.User;
import com.itransition.task4.service.UserService;
import com.itransition.task4.service.impl.UserServiceDefImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(proxyTargetClass = true, securedEnabled = true)
public class SecurityConfig implements WebMvcConfigurer {

    @Bean
    public UserService userService() {
        return new UserServiceDefImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        configureAuthManagerBuilder(http);

        http.authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                    .anyRequest().permitAll()
            )
            .formLogin(formLogin ->
                formLogin
                        .loginPage("/sign-in")
                        .loginProcessingUrl("/sign-in")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            User user = (User) authentication.getPrincipal();
                            user.setLastSignInDateTime(LocalDateTime.now());
                            userService().save(user);
                            response.sendRedirect("/");
                        })
                        .failureUrl("/sign-in?error")
                        .permitAll()
            )
            .logout(logout ->
                    logout
                            .logoutUrl("/sign-out")
                            .logoutSuccessUrl("/sign-in")
                            .permitAll()
            )
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    private void configureAuthManagerBuilder(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService()).passwordEncoder(passwordEncoder());
    }
}