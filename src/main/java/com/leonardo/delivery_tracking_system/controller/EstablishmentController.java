package com.leonardo.delivery_tracking_system.controller;

import com.leonardo.delivery_tracking_system.dto.establishment.EstablishmentRequest;
import com.leonardo.delivery_tracking_system.dto.establishment.EstablishmentResponse;
import com.leonardo.delivery_tracking_system.dto.establishment.EstablishmentUpdateDTO;
import com.leonardo.delivery_tracking_system.service.EstablishmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/establishments")
public class EstablishmentController {

    private final EstablishmentService establishmentService;

    public EstablishmentController(EstablishmentService establishmentService) {
        this.establishmentService = establishmentService;
    }

    @PostMapping
    public ResponseEntity<EstablishmentResponse> create(@Valid @RequestBody EstablishmentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(establishmentService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstablishmentResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(establishmentService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<EstablishmentResponse>> findAll(){
        return ResponseEntity.ok(establishmentService.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EstablishmentResponse> update(@PathVariable Long id, @Valid @RequestBody EstablishmentUpdateDTO request){
        return ResponseEntity.ok(establishmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        establishmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
