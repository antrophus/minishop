package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.User;
import com.mylittleshop.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User save(User user) { return userRepository.save(user); }
    public Optional<User> findById(Long id) { return userRepository.findById(id); }
    public List<User> findAll() { return userRepository.findAll(); }
    public void deleteById(Long id) { userRepository.deleteById(id); }
} 