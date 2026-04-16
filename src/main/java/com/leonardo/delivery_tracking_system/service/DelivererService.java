package com.leonardo.delivery_tracking_system.service;

import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererRequest;
import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererResponse;
import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererUpdateRequest;
import com.leonardo.delivery_tracking_system.exception.EntityNotFoundException;
import com.leonardo.delivery_tracking_system.mapper.DelivererMapper;
import com.leonardo.delivery_tracking_system.model.Deliverer;
import com.leonardo.delivery_tracking_system.repository.DelivererRepository;
import com.leonardo.delivery_tracking_system.utils.UpdateHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DelivererService {

    private final DelivererRepository delivererRepository;
    private final DelivererMapper delivererMapper;

    public DelivererService(DelivererRepository delivererRepository, DelivererMapper delivererMapper) {
        this.delivererRepository = delivererRepository;
        this.delivererMapper = delivererMapper;
    }

    private Deliverer findDelivererByIdOrThrow(Long id){
        return delivererRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Deliverer not found with ID: " + id));
    }

    public DelivererResponse create(DelivererRequest request){
        Deliverer deliverer = delivererMapper.toEntity(request);
        Deliverer savedDeliverer = delivererRepository.save(deliverer);
        return delivererMapper.toDto(savedDeliverer);
    }

    public DelivererResponse findById(Long id){
        Deliverer delivererFound = findDelivererByIdOrThrow(id);
        return delivererMapper.toDto(delivererFound);
    }

    public Page<DelivererResponse> findAll(Pageable pageable){
        Page<Deliverer> deliverers = delivererRepository.findAll(pageable);
        return deliverers.map(delivererMapper::toDto);
    }

    public DelivererResponse update(Long id, DelivererUpdateRequest request){
        Deliverer delivererExists = findDelivererByIdOrThrow(id);

        delivererExists.setName(UpdateHelper.getIfNotNull(request.name(), delivererExists.getName()));
        delivererExists.setPhone(UpdateHelper.getIfNotNull(request.phone(), delivererExists.getPhone()));
        delivererExists.setVehicleType(UpdateHelper.getIfNotNull(request.vehicleType(), delivererExists.getVehicleType()));

        Deliverer savedDeliverer = delivererRepository.save(delivererExists);
        return delivererMapper.toDto(savedDeliverer);
    }

    public void delete(Long id){
        Deliverer delivererExists = findDelivererByIdOrThrow(id);
        delivererRepository.delete(delivererExists);
    }
}
