package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Shipment;
import com.mylittleshop.backend.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;

    public Shipment save(Shipment shipment) { return shipmentRepository.save(shipment); }
    public Optional<Shipment> findById(Long id) { return shipmentRepository.findById(id); }
    public List<Shipment> findAll() { return shipmentRepository.findAll(); }
    public void deleteById(Long id) { shipmentRepository.deleteById(id); }
} 