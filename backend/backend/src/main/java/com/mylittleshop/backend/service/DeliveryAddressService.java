package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.DeliveryAddress;
import com.mylittleshop.backend.repository.DeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {
    private final DeliveryAddressRepository deliveryAddressRepository;

    public DeliveryAddress save(DeliveryAddress address) { return deliveryAddressRepository.save(address); }
    public Optional<DeliveryAddress> findById(Long id) { return deliveryAddressRepository.findById(id); }
    public List<DeliveryAddress> findAll() { return deliveryAddressRepository.findAll(); }
    public void deleteById(Long id) { deliveryAddressRepository.deleteById(id); }
} 