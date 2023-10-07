package com.itransition.task4.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationForm {

    private String firstName;

    private String lastName;

    private String position;

    private String email;

    private String password;

    private String confirmedPassword;
}
