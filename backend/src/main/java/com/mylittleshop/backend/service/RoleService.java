package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Role;
import com.mylittleshop.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role save(Role role) { return roleRepository.save(role); }
    public Optional<Role> findById(Long id) { return roleRepository.findById(id); }
    public List<Role> findAll() { return roleRepository.findAll(); }
    public void deleteById(Long id) { roleRepository.deleteById(id); }
} 