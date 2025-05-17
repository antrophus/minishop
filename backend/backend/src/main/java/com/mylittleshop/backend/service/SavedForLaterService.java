package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.SavedForLater;
import com.mylittleshop.backend.repository.SavedForLaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SavedForLaterService {
    private final SavedForLaterRepository savedForLaterRepository;

    public SavedForLater save(SavedForLater entity) { return savedForLaterRepository.save(entity); }
    public Optional<SavedForLater> findById(Long id) { return savedForLaterRepository.findById(id); }
    public List<SavedForLater> findAll() { return savedForLaterRepository.findAll(); }
    public void deleteById(Long id) { savedForLaterRepository.deleteById(id); }
} 