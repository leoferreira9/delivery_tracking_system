package com.leonardo.delivery_tracking_system.repository;

import com.leonardo.delivery_tracking_system.model.DeliveryStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryStatusHistoryRepository extends JpaRepository<DeliveryStatusHistory, Long> {
    List<DeliveryStatusHistory> findByDelivery_Id(Long id);
}
