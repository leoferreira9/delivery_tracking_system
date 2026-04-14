package com.leonardo.delivery_tracking_system.repository;

import com.leonardo.delivery_tracking_system.model.Establishment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {

    Optional<Establishment> findByCnpj(String cnpj);
}
