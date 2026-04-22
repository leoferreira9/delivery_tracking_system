package com.leonardo.delivery_tracking_system.controller;

import com.leonardo.delivery_tracking_system.dto.establishment.EstablishmentRequest;
import com.leonardo.delivery_tracking_system.dto.establishment.EstablishmentResponse;
import com.leonardo.delivery_tracking_system.dto.establishment.EstablishmentUpdateDTO;
import com.leonardo.delivery_tracking_system.service.EstablishmentService;
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
@RequestMapping("/v1/establishments")
@Tag(name = "Establishments", description = "Establishment management operations")
public class EstablishmentController {

    private final EstablishmentService establishmentService;

    public EstablishmentController(EstablishmentService establishmentService) {
        this.establishmentService = establishmentService;
    }

    @Operation(summary = "Create a new establishment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Establishment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Establishment already exists")
    })
    @PostMapping
    public ResponseEntity<EstablishmentResponse> create(@Valid @RequestBody EstablishmentRequest request){
        EstablishmentResponse establishmentCreated = establishmentService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(establishmentCreated.id())
                .toUri();

        return ResponseEntity.created(location).body(establishmentCreated);
    }

    @Operation(summary = "Get establishment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Establishment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Establishment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstablishmentResponse> findById(@Parameter(description = "Establishment ID", example = "1") @PathVariable Long id){
        return ResponseEntity.ok(establishmentService.findById(id));
    }

    @Operation(summary = "Get all establishments (paginated)")
    @ApiResponse(responseCode = "200", description = "Establishments retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<EstablishmentResponse>> findAll(@PageableDefault(sort = "name") Pageable pageable){
        return ResponseEntity.ok(establishmentService.findAll(pageable));
    }

    @Operation(summary = "Update establishment data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Establishment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Establishment not found"),
            @ApiResponse(responseCode = "409", description = "CNPJ already registered")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<EstablishmentResponse> update(
            @Parameter(description = "Establishment ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody EstablishmentUpdateDTO request)
    {
        return ResponseEntity.ok(establishmentService.update(id, request));
    }

    @Operation(summary = "Delete establishment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Establishment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Establishment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "Establishment ID", example = "1") @PathVariable Long id){
        establishmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
