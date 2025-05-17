package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Permission;
import com.mylittleshop.backend.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Permission save(Permission permission) { return permissionRepository.save(permission); }
    public Optional<Permission> findById(Long id) { return permissionRepository.findById(id); }
    public List<Permission> findAll() { return permissionRepository.findAll(); }
    public void deleteById(Long id) { permissionRepository.deleteById(id); }
} 