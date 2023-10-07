package com.itransition.task4.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter @Setter
@Table(name = "permissions")
public class Permission implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permission_level")
    private String permissionLevel;

    @Column(name = "role")
    private String role;

    @Override
    public String getAuthority() {
        return this.permissionLevel;
    }
}
