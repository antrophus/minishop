package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
} 