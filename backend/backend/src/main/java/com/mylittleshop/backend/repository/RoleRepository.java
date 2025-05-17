package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
} 