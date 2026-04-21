package com.leonardo.delivery_tracking_system.service;

import com.leonardo.delivery_tracking_system.dto.establishment.EstablishmentRequest;
import com.leonardo.delivery_tracking_system.dto.establishment.EstablishmentResponse;
import com.leonardo.delivery_tracking_system.dto.establishment.EstablishmentUpdateDTO;
import com.leonardo.delivery_tracking_system.exception.EntityAlreadyRegisteredException;
import com.leonardo.delivery_tracking_system.exception.EntityNotFoundException;
import com.leonardo.delivery_tracking_system.mapper.EstablishmentMapper;
import com.leonardo.delivery_tracking_system.model.Address;
import com.leonardo.delivery_tracking_system.model.Establishment;
import com.leonardo.delivery_tracking_system.repository.EstablishmentRepository;
import com.leonardo.delivery_tracking_system.utils.UpdateHelper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class EstablishmentService {

    private final EstablishmentRepository repository;
    private final EstablishmentMapper mapper;

    public EstablishmentService(EstablishmentRepository repository, EstablishmentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Establishment findEstablishmentByIdOrThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Establishment not found with ID: " + id));
    }

    private boolean validateCnpjAlreadyExists(String cnpj){
        return repository.findByCnpj(cnpj).isPresent();
    }

    private boolean validateCnpjForUpdate(String newCnpj, Establishment currentEstablishment){
        Optional<Establishment> establishmentExists = repository.findByCnpj(newCnpj);
        return establishmentExists.isEmpty() || establishmentExists.get().getId().equals(currentEstablishment.getId());
    }

    public EstablishmentResponse create(EstablishmentRequest request){
        log.info("Creating establishment with CNPJ: {}", request.cnpj());
        if(validateCnpjAlreadyExists(request.cnpj())) throw new EntityAlreadyRegisteredException("CNPJ: " + request.cnpj() + " already registered!");

        Establishment establishment = mapper.toEntity(request);
        Establishment savedEstablishment = repository.save(establishment);

        log.info("Establishment created successfully. ID: {}, CNPJ: {}", savedEstablishment.getId(), savedEstablishment.getCnpj());
        return mapper.toDto(savedEstablishment);
    }

    public EstablishmentResponse findById(Long id){
        log.info("Getting establishment by ID: {}", id);
        Establishment establishmentFound = findEstablishmentByIdOrThrow(id);

        log.info("Establishment found. ID: {}, CNPJ: {}, Name: {}", establishmentFound.getId(), establishmentFound.getCnpj(), establishmentFound.getName());
        return mapper.toDto(establishmentFound);
    }

    public Page<EstablishmentResponse> findAll(Pageable pageable){
        log.info("Getting all establishments. Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Establishment> establishments = repository.findAll(pageable);

        log.info("Found {} establishments", establishments.getNumberOfElements());
        return establishments.map(mapper::toDto);
    }

    @Transactional
    public EstablishmentResponse update(Long id, EstablishmentUpdateDTO request){
        log.info("Updating establishment data by ID: {}", id);
        Establishment establishmentExists = findEstablishmentByIdOrThrow(id);

        if(request.cnpj() != null){
            if(!validateCnpjForUpdate(request.cnpj(), establishmentExists))
                throw new EntityAlreadyRegisteredException("CNPJ: " + request.cnpj() + " already registered!");
        }

        establishmentExists.setCnpj(UpdateHelper.getIfNotNull(request.cnpj(), establishmentExists.getCnpj()));
        establishmentExists.setName(UpdateHelper.getIfNotNull(request.name(), establishmentExists.getName()));
        establishmentExists.setPhone(UpdateHelper.getIfNotNull(request.phone(), establishmentExists.getPhone()));

        if(request.address() != null){
            log.info("Updating establishment address");
            if(establishmentExists.getAddress() != null){
                Address addressExists = establishmentExists.getAddress();

                addressExists.setNeighborhood(UpdateHelper.getIfNotNull(request.address().neighborhood(), addressExists.getNeighborhood()));
                addressExists.setCity(UpdateHelper.getIfNotNull(request.address().city(), addressExists.getCity()));
                addressExists.setStreet(UpdateHelper.getIfNotNull(request.address().street(), addressExists.getStreet()));
                addressExists.setNumber(UpdateHelper.getIfNotNull(request.address().number(), addressExists.getNumber()));
                addressExists.setZipCode(UpdateHelper.getIfNotNull(request.address().zipCode(), addressExists.getZipCode()));
            } else {
                Address newAddress = new Address();

                newAddress.setZipCode(request.address().zipCode());
                newAddress.setNeighborhood(request.address().neighborhood());
                newAddress.setCity(request.address().city());
                newAddress.setStreet(request.address().street());
                newAddress.setNumber(request.address().number());
                establishmentExists.setAddress(newAddress);
            }
        }

        Establishment savedEstablishment = repository.save(establishmentExists);

        log.info("Establishment updated successfully. ID: {}, CNPJ: {}, Name: {}", savedEstablishment.getId(), savedEstablishment.getCnpj(), savedEstablishment.getName());
        return mapper.toDto(savedEstablishment);
    }

    @Transactional
    public void delete(Long id){
        log.info("Deleting establishment");
        Establishment establishmentExists = findEstablishmentByIdOrThrow(id);

        repository.delete(establishmentExists);
        log.info("Establishment deleted successfully. ID: {}", id);
    }
}
