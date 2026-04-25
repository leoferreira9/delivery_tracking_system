package com.leonardo.delivery_tracking_system.service;

import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererRequest;
import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererResponse;
import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererUpdateRequest;
import com.leonardo.delivery_tracking_system.exception.EntityNotFoundException;
import com.leonardo.delivery_tracking_system.mapper.DelivererMapper;
import com.leonardo.delivery_tracking_system.model.Deliverer;
import com.leonardo.delivery_tracking_system.repository.DelivererRepository;
import com.leonardo.delivery_tracking_system.utils.UpdateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DelivererService {

    private final DelivererRepository delivererRepository;
    private final DelivererMapper delivererMapper;

    public DelivererService(DelivererRepository delivererRepository, DelivererMapper delivererMapper) {
        this.delivererRepository = delivererRepository;
        this.delivererMapper = delivererMapper;
    }

    public Deliverer findDelivererByIdOrThrow(Long id){
        return delivererRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Deliverer not found with ID: " + id));
    }

    public DelivererResponse create(DelivererRequest request){
        log.info("Creating deliverer");
        Deliverer deliverer = delivererMapper.toEntity(request);
        Deliverer savedDeliverer = delivererRepository.save(deliverer);

        log.info("Deliverer created successfully. ID: {}, Name: {}", savedDeliverer.getId(), savedDeliverer.getName());
        return delivererMapper.toDto(savedDeliverer);
    }

    public DelivererResponse findById(Long id){
        log.info("Getting deliverer by ID: {}", id);
        Deliverer delivererFound = findDelivererByIdOrThrow(id);

        log.info("Deliverer found. ID: {}, Name: {}", delivererFound.getId(), delivererFound.getName());
        return delivererMapper.toDto(delivererFound);
    }

    public Page<DelivererResponse> findAll(Pageable pageable){
        log.info("Getting all deliverers. Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Deliverer> deliverers = delivererRepository.findAll(pageable);

        log.info("Found {} deliverers", deliverers.getNumberOfElements());
        return deliverers.map(delivererMapper::toDto);
    }

    @Transactional
    public DelivererResponse update(Long id, DelivererUpdateRequest request){
        log.info("Updating deliverer data by ID: {}", id);
        Deliverer delivererExists = findDelivererByIdOrThrow(id);

        delivererExists.setName(UpdateHelper.getIfNotNull(request.name(), delivererExists.getName()));
        delivererExists.setPhone(UpdateHelper.getIfNotNull(request.phone(), delivererExists.getPhone()));
        delivererExists.setVehicleType(UpdateHelper.getIfNotNull(request.vehicleType(), delivererExists.getVehicleType()));

        Deliverer savedDeliverer = delivererRepository.save(delivererExists);

        log.info("Deliverer updated successfully. ID: {}, Name: {}", savedDeliverer.getId(), savedDeliverer.getName());
        return delivererMapper.toDto(savedDeliverer);
    }

    @Transactional
    public void delete(Long id){
        log.info("Deleting deliverer");
        Deliverer delivererExists = findDelivererByIdOrThrow(id);

        delivererRepository.delete(delivererExists);
        log.info("Deliverer deleted successfully. ID: {}", id);
    }
}
