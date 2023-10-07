package com.itransition.task4.service.impl;

import com.itransition.task4.model.Permission;
import com.itransition.task4.repository.PermissionRepository;
import com.itransition.task4.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceDefImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public Permission getAdminPermission() {
        return permissionRepository.findPermissionByPermissionLevel("ROLE_ADMIN");
    }
}
