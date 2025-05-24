package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Delivery;
import com.mylittleshop.backend.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    public Delivery save(Delivery delivery) { return deliveryRepository.save(delivery); }
    public Optional<Delivery> findById(Long id) { return deliveryRepository.findById(id); }
    public List<Delivery> findAll() { return deliveryRepository.findAll(); }
    public void deleteById(Long id) { deliveryRepository.deleteById(id); }
} 