package com.leonardo.delivery_tracking_system.controller;

import com.leonardo.delivery_tracking_system.dto.delivery.AssignDelivererDTO;
import com.leonardo.delivery_tracking_system.dto.delivery.DeliveryRequest;
import com.leonardo.delivery_tracking_system.dto.delivery.DeliveryResponse;
import com.leonardo.delivery_tracking_system.dto.delivery.UpdateDeliveryStatusDTO;
import com.leonardo.delivery_tracking_system.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public ResponseEntity<DeliveryResponse> create(@Valid @RequestBody DeliveryRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<DeliveryResponse>> findAll(@PageableDefault(sort = "createdAt") Pageable pageable){
        return ResponseEntity.ok(deliveryService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(deliveryService.findById(id));
    }

    @GetMapping("/tracking/{trackingCode}")
    public ResponseEntity<DeliveryResponse> findByTrackingCode(@PathVariable String trackingCode){
        return ResponseEntity.ok(deliveryService.findByTrackingCode(trackingCode));
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<DeliveryResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateDeliveryStatusDTO request){
        return ResponseEntity.ok(deliveryService.updateStatus(id, request.status()));
    }

    @PatchMapping("/{id}/assign-deliverer")
    public ResponseEntity<DeliveryResponse> assignDeliverer(@PathVariable Long id, @Valid @RequestBody AssignDelivererDTO request){
        return ResponseEntity.ok(deliveryService.assignDeliverer(id, request.delivererId()));
    }


}
