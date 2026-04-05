package com.leonardo.delivery_tracking_system.repository;

import com.leonardo.delivery_tracking_system.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {}
