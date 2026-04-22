package com.leonardo.delivery_tracking_system.service;

import com.leonardo.delivery_tracking_system.dto.delivery.DeliveryRequest;
import com.leonardo.delivery_tracking_system.dto.delivery.DeliveryResponse;
import com.leonardo.delivery_tracking_system.dto.delivery.DeliveryStatusHistoryResponse;
import com.leonardo.delivery_tracking_system.enums.DeliveryStatus;
import com.leonardo.delivery_tracking_system.exception.EntityNotFoundException;
import com.leonardo.delivery_tracking_system.exception.FailedToAssignDelivererException;
import com.leonardo.delivery_tracking_system.exception.FailedToUpdateDeliveryStatusException;
import com.leonardo.delivery_tracking_system.mapper.DeliveryMapper;
import com.leonardo.delivery_tracking_system.mapper.DeliveryStatusHistoryMapper;
import com.leonardo.delivery_tracking_system.model.*;
import com.leonardo.delivery_tracking_system.repository.DeliveryRepository;
import com.leonardo.delivery_tracking_system.repository.DeliveryStatusHistoryRepository;
import com.leonardo.delivery_tracking_system.specification.DeliverySpecifications;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class DeliveryService {

    private final DeliveryMapper deliveryMapper;
    private final DeliveryRepository deliveryRepository;
    private final CustomerService customerService;
    private final EstablishmentService establishmentService;
    private final DelivererService delivererService;
    private final DeliveryStatusHistoryRepository deliveryStatusHistoryRepository;
    private final DeliveryStatusHistoryMapper deliveryStatusHistoryMapper;

    public DeliveryService(DeliveryMapper deliveryMapper,
                           DeliveryRepository deliveryRepository,
                           CustomerService customerService,
                           EstablishmentService establishmentService,
                           DelivererService delivererService,
                           DeliveryStatusHistoryRepository deliveryStatusHistoryRepository,
                           DeliveryStatusHistoryMapper deliveryStatusHistoryMapper) {
        this.deliveryMapper = deliveryMapper;
        this.deliveryRepository = deliveryRepository;
        this.customerService = customerService;
        this.establishmentService = establishmentService;
        this.delivererService = delivererService;
        this.deliveryStatusHistoryRepository = deliveryStatusHistoryRepository;
        this.deliveryStatusHistoryMapper = deliveryStatusHistoryMapper;
    }

    private Delivery findDeliveryByIdOrThrow(Long id){
        return deliveryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Delivery not found with ID: " + id));
    }

    private String generateTrackingCode(){
        return UUID.randomUUID().toString().replace("-", "").substring(0, 15).toUpperCase();
    }

    private DeliveryStatusHistory buildDeliveryStatusHistory(Delivery delivery){
        DeliveryStatusHistory deliveryStatusHistory = new DeliveryStatusHistory();
        deliveryStatusHistory.setStatus(delivery.getStatus());
        deliveryStatusHistory.setDelivery(delivery);
        deliveryStatusHistory.setChangedAt(LocalDateTime.now());

        return deliveryStatusHistory;
    }

    private static final Map<DeliveryStatus, Set<DeliveryStatus>> allowedStatus = new EnumMap<>(DeliveryStatus.class);

    static {
        allowedStatus.put(DeliveryStatus.RECEIVED, EnumSet.of(DeliveryStatus.PREPARING, DeliveryStatus.CANCELLED));
        allowedStatus.put(DeliveryStatus.PREPARING, EnumSet.of(DeliveryStatus.IN_TRANSIT, DeliveryStatus.DELIVERED, DeliveryStatus.CANCELLED));
        allowedStatus.put(DeliveryStatus.IN_TRANSIT, EnumSet.of(DeliveryStatus.DELIVERED));
        allowedStatus.put(DeliveryStatus.DELIVERED, EnumSet.noneOf(DeliveryStatus.class));
        allowedStatus.put(DeliveryStatus.CANCELLED, EnumSet.noneOf(DeliveryStatus.class));
    }

    @Transactional
    public DeliveryResponse create(DeliveryRequest request){
        log.info("Creating delivery");
        Customer customer = customerService.findCustomerByIdOrThrow(request.customerId());
        Establishment establishment = establishmentService.findEstablishmentByIdOrThrow(request.establishmentId());

        String trackingCode;

        do{
            trackingCode = generateTrackingCode();
        } while(deliveryRepository.findByTrackingCode(trackingCode).isPresent());

        Delivery delivery = new Delivery();
        delivery.setCustomer(customer);
        delivery.setStatus(DeliveryStatus.RECEIVED);
        delivery.setEstablishment(establishment);
        delivery.setTrackingCode(trackingCode);
        delivery.setDeliverer(null);
        delivery.setDeliveredAt(null);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        DeliveryStatusHistory deliveryStatusHistory = buildDeliveryStatusHistory(savedDelivery);
        deliveryStatusHistoryRepository.save(deliveryStatusHistory);

        log.info("Delivery created successfully. ID: {}, Tracking code: {}", savedDelivery.getId(), savedDelivery.getTrackingCode());
        return deliveryMapper.toDto(savedDelivery);
    }

    public DeliveryResponse findById(Long id){
        log.info("Getting delivery by ID: {}", id);
        Delivery deliveryFound = findDeliveryByIdOrThrow(id);

        log.info("Delivery found. ID: {}", id);
        return deliveryMapper.toDto(deliveryFound);
    }

    public Page<DeliveryResponse> findAll(DeliveryStatus status, Long establishmentId, LocalDate startDate, LocalDate endDate, Pageable pageable){

        Page<Delivery> deliveries;

        Specification<Delivery> spec = DeliverySpecifications.hasStatus(status)
                .and(DeliverySpecifications.hasEstablishmentId(establishmentId))
                .and(DeliverySpecifications.isBetween(startDate, endDate));

        deliveries = deliveryRepository.findAll(spec, pageable);

        log.info("Found {} deliveries", deliveries.getNumberOfElements());
        return deliveries.map(deliveryMapper::toDto);
    }

    public DeliveryResponse findByTrackingCode(String trackingCode){
        log.info("Getting delivery by tracking code: {}", trackingCode);
        Delivery deliveryFound = deliveryRepository.findByTrackingCode(trackingCode)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found with tracking code: " + trackingCode));

        log.info("Delivery found. ID: {}, Tracking code: {}", deliveryFound.getId(), deliveryFound.getTrackingCode());
        return deliveryMapper.toDto(deliveryFound);
    }

    @Transactional
    public DeliveryResponse updateStatus(Long id, DeliveryStatus newStatus){
        log.info("Updating delivery data by ID: {}", id);
        Delivery deliveryExists = findDeliveryByIdOrThrow(id);

        Set<DeliveryStatus> allowedDeliveryStatus = allowedStatus.get(deliveryExists.getStatus());

        if(allowedDeliveryStatus != null && allowedDeliveryStatus.contains(newStatus)){
            if((newStatus == DeliveryStatus.IN_TRANSIT || newStatus == DeliveryStatus.DELIVERED) && deliveryExists.getDeliverer() == null)
                throw new FailedToUpdateDeliveryStatusException("The delivery must include the deliverer ID!");

            if(newStatus == DeliveryStatus.DELIVERED){
                log.info("Setting deliveredAt for delivery ID: {}", id);
                deliveryExists.setDeliveredAt(LocalDateTime.now());
            }

            deliveryExists.setStatus(newStatus);
        } else {
            throw new FailedToUpdateDeliveryStatusException
                    ("Cannot update delivery status from (" + deliveryExists.getStatus() + ") to (" + newStatus + "). " +
                            "Allowed status: " + allowedDeliveryStatus);
        }

        Delivery savedDelivery = deliveryRepository.save(deliveryExists);

        DeliveryStatusHistory deliveryStatusHistory = buildDeliveryStatusHistory(savedDelivery);
        deliveryStatusHistoryRepository.save(deliveryStatusHistory);

        log.info("Delivery status updated successfully. ID: {}, Status: {}", id, newStatus);
        return deliveryMapper.toDto(savedDelivery);
    }

    @Transactional
    public DeliveryResponse assignDeliverer(Long id, Long delivererId){
        log.info("Assigning deliverer ID: {} to delivery ID: {}", delivererId, id);
        Delivery deliveryExists = findDeliveryByIdOrThrow(id);
        Deliverer delivererExists = delivererService.findDelivererByIdOrThrow(delivererId);

        if(deliveryExists.getStatus() == DeliveryStatus.PREPARING){
            deliveryExists.setDeliverer(delivererExists);
        } else {
            throw new FailedToAssignDelivererException("Failed to assign deliverer, delivery status must be " + DeliveryStatus.PREPARING);
        }

        Delivery savedDelivery = deliveryRepository.save(deliveryExists);

        log.info("Deliverer ID: {} assigned successfully to delivery ID: {}", delivererId, id);
        return deliveryMapper.toDto(savedDelivery);
    }

    public List<DeliveryStatusHistoryResponse> getDeliveryStatusHistory(Long id){
        findDeliveryByIdOrThrow(id);
        return deliveryStatusHistoryRepository.findByDelivery_Id(id).stream().map(deliveryStatusHistoryMapper::toDto).toList();
    }
}
