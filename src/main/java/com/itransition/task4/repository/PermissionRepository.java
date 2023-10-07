package com.itransition.task4.repository;

import com.itransition.task4.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findPermissionByPermissionLevel(String permissionLevel);
}
