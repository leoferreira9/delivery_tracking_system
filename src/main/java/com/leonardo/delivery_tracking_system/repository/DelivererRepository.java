package com.leonardo.delivery_tracking_system.repository;

import com.leonardo.delivery_tracking_system.model.Deliverer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DelivererRepository extends JpaRepository<Deliverer, Long> {}
