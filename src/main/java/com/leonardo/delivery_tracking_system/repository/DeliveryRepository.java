package com.leonardo.delivery_tracking_system.repository;

import com.leonardo.delivery_tracking_system.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {}
