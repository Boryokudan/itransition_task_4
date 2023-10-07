package com.itransition.task4.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class UserDTO {

    private Long id;

    private String fullName;

    private String position;

    private String email;

    private String status;

    private LocalDateTime registrationDateTime;

    private LocalDateTime lastSignInDateTime;
}
