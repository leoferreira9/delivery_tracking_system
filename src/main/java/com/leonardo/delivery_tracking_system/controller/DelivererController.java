package com.leonardo.delivery_tracking_system.controller;

import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererRequest;
import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererResponse;
import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererUpdateRequest;
import com.leonardo.delivery_tracking_system.service.DelivererService;
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

@RestController
@RequestMapping("/v1/deliverers")
@Tag(name = "Deliverers", description = "Deliverer management operations")
public class DelivererController {

    private final DelivererService delivererService;

    public DelivererController(DelivererService delivererService) {
        this.delivererService = delivererService;
    }

    @Operation(summary = "Create a new deliverer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Deliverer successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<DelivererResponse> create(@Valid @RequestBody DelivererRequest request){
        DelivererResponse delivererCreated = delivererService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(delivererCreated.id())
                .toUri();

        return ResponseEntity.created(location).body(delivererCreated);
    }

    @Operation(summary = "Get deliverer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deliverer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Deliverer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DelivererResponse> findById(@Parameter(description = "Deliverer ID", example = "1") @PathVariable Long id){
        return ResponseEntity.ok(delivererService.findById(id));
    }

    @Operation(summary = "Get all deliverers (paginated)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deliverers retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<DelivererResponse>> findAll(@PageableDefault(sort = "name") Pageable pageable){
        return ResponseEntity.ok(delivererService.findAll(pageable));
    }

    @Operation(summary = "Update deliverer data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deliverer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Deliverer not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<DelivererResponse> update(
            @Parameter(description = "Deliverer ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody DelivererUpdateRequest request)
    {
        return ResponseEntity.ok(delivererService.update(id, request));
    }

    @Operation(summary = "Delete deliverer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deliverer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Deliverer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "Deliverer ID", example = "1") @PathVariable Long id){
        delivererService.delete(id);
        return ResponseEntity.noContent().build();
    }
}



