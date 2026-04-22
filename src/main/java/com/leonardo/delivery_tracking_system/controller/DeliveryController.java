package com.leonardo.delivery_tracking_system.controller;

import com.leonardo.delivery_tracking_system.dto.delivery.*;
import com.leonardo.delivery_tracking_system.enums.DeliveryStatus;
import com.leonardo.delivery_tracking_system.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/deliveries")
@Tag(name = "Deliveries", description = "Deliveries management operations")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Operation(summary = "Create a new delivery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Delivery created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
    })
    @PostMapping
    public ResponseEntity<DeliveryResponse> create(@Valid @RequestBody DeliveryRequest request){
        DeliveryResponse deliveryCreated = deliveryService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(deliveryCreated.id())
                .toUri();

        return ResponseEntity.created(location).body(deliveryCreated);
    }

    @Operation(summary = "Get all deliveries")
    @ApiResponse(responseCode = "200", description = "Deliveries retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<DeliveryResponse>> findAll(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) DeliveryStatus status,
            @RequestParam(required = false) Long establishmentId,
            @PageableDefault(sort = "createdAt") Pageable pageable){
        return ResponseEntity.ok(deliveryService.findAll(status, establishmentId, startDate, endDate, pageable));
    }

    @Operation(summary = "Get delivery by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Delivery not found"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> findById(@Parameter(description = "Delivery ID", example = "1") @PathVariable Long id){
        return ResponseEntity.ok(deliveryService.findById(id));
    }

    @Operation(summary = "Get delivery by tracking code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Delivery not found"),
    })
    @GetMapping("/tracking/{trackingCode}")
    public ResponseEntity<DeliveryResponse> findByTrackingCode(@Parameter(description = "Delivery tracking code", example = "A4DCBC52788F498") @PathVariable String trackingCode){
        return ResponseEntity.ok(deliveryService.findByTrackingCode(trackingCode));
    }

    @Operation(summary = "Update delivery data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Delivery not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<DeliveryResponse> updateStatus(
            @Parameter(description = "Delivery ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateDeliveryStatusDTO request)
    {
        return ResponseEntity.ok(deliveryService.updateStatus(id, request.status()));
    }

    @Operation(summary = "Assign deliverer to delivery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deliverer assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Delivery not found or deliverer not found")
    })
    @PatchMapping("/{id}/assign-deliverer")
    public ResponseEntity<DeliveryResponse> assignDeliverer(
            @Parameter(description = "Delivery ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody AssignDelivererDTO request)
    {
        return ResponseEntity.ok(deliveryService.assignDeliverer(id, request.delivererId()));
    }


    @GetMapping("/{id}/history")
    public ResponseEntity<List<DeliveryStatusHistoryResponse>> getDeliveryStatusHistory(@PathVariable Long id){
        return ResponseEntity.ok(deliveryService.getDeliveryStatusHistory(id));
    }
}
