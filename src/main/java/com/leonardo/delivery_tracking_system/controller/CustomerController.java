package com.leonardo.delivery_tracking_system.controller;

import com.leonardo.delivery_tracking_system.dto.customer.CustomerRequest;
import com.leonardo.delivery_tracking_system.dto.customer.CustomerResponse;
import com.leonardo.delivery_tracking_system.dto.customer.CustomerUpdateDTO;
import com.leonardo.delivery_tracking_system.service.CustomerService;
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
@RequestMapping("/v1/customers")
@Tag(name = "Customers", description = "Customer management operations")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Customer already exists")
    })
    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody @Valid CustomerRequest request){
        CustomerResponse customerCreated = customerService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerCreated.id())
                .toUri();

        return ResponseEntity.created(location).body(customerCreated);
    }

    @Operation(summary = "Get all customers (paginated)")
    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> findAll(@PageableDefault(sort = "name") Pageable pageable){
        return ResponseEntity.ok(customerService.findAll(pageable));
    }

    @Operation(summary = "Get customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@Parameter(description = "Customer ID", example = "1") @PathVariable Long id){
        return ResponseEntity.ok(customerService.findById(id));
    }

    @Operation(summary = "Update customer data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "409", description = "CPF already registered")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
           @Parameter(description = "Customer ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateDTO request)
    {
        return ResponseEntity.ok(customerService.update(id, request));
    }

    @Operation(summary = "Delete customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "Customer ID", example = "1") @PathVariable Long id){
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
