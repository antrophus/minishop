package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.UserProfile;
import com.mylittleshop.backend.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfile save(UserProfile userProfile) { return userProfileRepository.save(userProfile); }
    public Optional<UserProfile> findById(Long id) { return userProfileRepository.findById(id); }
    public List<UserProfile> findAll() { return userProfileRepository.findAll(); }
    public void deleteById(Long id) { userProfileRepository.deleteById(id); }
} 