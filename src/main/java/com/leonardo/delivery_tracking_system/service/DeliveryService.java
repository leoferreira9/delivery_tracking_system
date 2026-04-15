package com.leonardo.delivery_tracking_system.service;

import com.leonardo.delivery_tracking_system.dto.delivery.DeliveryRequest;
import com.leonardo.delivery_tracking_system.dto.delivery.DeliveryResponse;
import com.leonardo.delivery_tracking_system.enums.DeliveryStatus;
import com.leonardo.delivery_tracking_system.exception.EntityNotFoundException;
import com.leonardo.delivery_tracking_system.exception.FailedToAssignDelivererException;
import com.leonardo.delivery_tracking_system.exception.FailedToUpdateDeliveryStatusException;
import com.leonardo.delivery_tracking_system.mapper.DeliveryMapper;
import com.leonardo.delivery_tracking_system.model.Customer;
import com.leonardo.delivery_tracking_system.model.Deliverer;
import com.leonardo.delivery_tracking_system.model.Delivery;
import com.leonardo.delivery_tracking_system.model.Establishment;
import com.leonardo.delivery_tracking_system.repository.CustomerRepository;
import com.leonardo.delivery_tracking_system.repository.DelivererRepository;
import com.leonardo.delivery_tracking_system.repository.DeliveryRepository;
import com.leonardo.delivery_tracking_system.repository.EstablishmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DeliveryService {

    private final DeliveryMapper deliveryMapper;
    private final DeliveryRepository deliveryRepository;
    private final CustomerRepository customerRepository;
    private final EstablishmentRepository establishmentRepository;
    private final DelivererRepository delivererRepository;

    public DeliveryService(DeliveryMapper deliveryMapper,
                           DeliveryRepository deliveryRepository,
                           CustomerRepository customerRepository,
                           EstablishmentRepository establishmentRepository,
                           DelivererRepository delivererRepository) {
        this.deliveryMapper = deliveryMapper;
        this.deliveryRepository = deliveryRepository;
        this.customerRepository = customerRepository;
        this.establishmentRepository = establishmentRepository;
        this.delivererRepository = delivererRepository;
    }

    private Delivery findDeliveryByIdOrThrow(Long id){
        return deliveryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Delivery not found with ID: " + id));
    }

    private static final Map<DeliveryStatus, Set<DeliveryStatus>> allowedStatus = new EnumMap<>(DeliveryStatus.class);

    static {
        allowedStatus.put(DeliveryStatus.RECEIVED, EnumSet.of(DeliveryStatus.PREPARING, DeliveryStatus.CANCELLED));
        allowedStatus.put(DeliveryStatus.PREPARING, EnumSet.of(DeliveryStatus.IN_TRANSIT, DeliveryStatus.DELIVERED, DeliveryStatus.CANCELLED));
        allowedStatus.put(DeliveryStatus.IN_TRANSIT, EnumSet.of(DeliveryStatus.DELIVERED));
    }

    @Transactional
    public DeliveryResponse create(DeliveryRequest request){
        Customer customer = customerRepository.findById(request.customerId()).orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + request.customerId()));
        Establishment establishment = establishmentRepository.findById(request.establishmentId()).orElseThrow(() -> new EntityNotFoundException("Establishment not found with ID: " + request.establishmentId()));
        String trackingCode = UUID.randomUUID().toString().substring(0, 15);

        Delivery delivery = new Delivery();
        delivery.setCreatedAt(LocalDateTime.now());
        delivery.setCustomer(customer);
        delivery.setStatus(DeliveryStatus.RECEIVED);
        delivery.setEstablishment(establishment);
        delivery.setTrackingCode(trackingCode);
        delivery.setDeliverer(null);
        delivery.setDeliveredAt(null);

        Delivery savedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(savedDelivery);
    }

    public DeliveryResponse findById(Long id){
        Delivery deliveryFound = findDeliveryByIdOrThrow(id);
        return deliveryMapper.toDto(deliveryFound);
    }

    public DeliveryResponse updateStatus(Long id, DeliveryStatus newStatus){
        Delivery deliveryExists = findDeliveryByIdOrThrow(id);

        Set<DeliveryStatus> allowedDeliveryStatus = allowedStatus.get(deliveryExists.getStatus());

        if(allowedDeliveryStatus != null && allowedDeliveryStatus.contains(newStatus)){
            if((newStatus == DeliveryStatus.IN_TRANSIT || newStatus == DeliveryStatus.DELIVERED) && deliveryExists.getDeliverer() == null)
                throw new FailedToUpdateDeliveryStatusException("The delivery must include the deliverer ID!");

            if(newStatus == DeliveryStatus.DELIVERED){
                deliveryExists.setDeliveredAt(LocalDateTime.now());
            }

            deliveryExists.setStatus(newStatus);
        } else {
            throw new FailedToUpdateDeliveryStatusException
                    ("Cannot update delivery status from (" + deliveryExists.getStatus() + ") to (" + newStatus + "). " +
                            "Allowed status: " + allowedDeliveryStatus);
        }

        Delivery savedDelivery = deliveryRepository.save(deliveryExists);

        return deliveryMapper.toDto(savedDelivery);
    }

    public DeliveryResponse assignDeliverer(Long id, Long delivererId){
        Delivery deliveryExists = findDeliveryByIdOrThrow(id);
        Deliverer delivererExists = delivererRepository.findById(delivererId).orElseThrow(() -> new EntityNotFoundException("Deliverer not found with ID: " + delivererId));

        if(deliveryExists.getStatus() == DeliveryStatus.PREPARING){
            deliveryExists.setDeliverer(delivererExists);
        } else {
            throw new FailedToAssignDelivererException("Failed to assign deliverer, delivery status must be " + DeliveryStatus.PREPARING);
        }

        Delivery savedDelivery = deliveryRepository.save(deliveryExists);
        return deliveryMapper.toDto(savedDelivery);
    }
}
