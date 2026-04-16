package com.leonardo.delivery_tracking_system.controller;

import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererRequest;
import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererResponse;
import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererUpdateRequest;
import com.leonardo.delivery_tracking_system.service.DelivererService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/deliverers")
public class DelivererController {

    private final DelivererService delivererService;

    public DelivererController(DelivererService delivererService) {
        this.delivererService = delivererService;
    }

    @PostMapping
    public ResponseEntity<DelivererResponse> create(@Valid @RequestBody DelivererRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(delivererService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DelivererResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(delivererService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<DelivererResponse>> findAll(@PageableDefault(sort = "name") Pageable pageable){
        return ResponseEntity.ok(delivererService.findAll(pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DelivererResponse> update(@PathVariable Long id, @Valid @RequestBody DelivererUpdateRequest request){
        return ResponseEntity.ok(delivererService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        delivererService.delete(id);
        return ResponseEntity.noContent().build();
    }
}



