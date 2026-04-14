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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstablishmentService {

    private final EstablishmentRepository repository;
    private final EstablishmentMapper mapper;

    public EstablishmentService(EstablishmentRepository repository, EstablishmentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    private Establishment findEstablishmentByIdOrThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Establishment not found with ID: " + id));
    }

    private boolean validateCnpjAlreadyExists(String cnpj){
        return repository.findByCnpj(cnpj).isPresent();
    }

    private boolean validateCnpjForUpdate(String newCnpj, Establishment currentEstablishment){
        Optional<Establishment> establishmentExists = repository.findByCnpj(newCnpj);
        return establishmentExists.isPresent() && establishmentExists.get().getId().equals(currentEstablishment.getId());
    }

    public EstablishmentResponse create(EstablishmentRequest request){
        if(validateCnpjAlreadyExists(request.cnpj())) throw new EntityAlreadyRegisteredException("CNPJ: " + request.cnpj() + " already registered!");

        Establishment establishment = mapper.toEntity(request);
        Establishment savedEstablishment = repository.save(establishment);
        return mapper.toDto(savedEstablishment);
    }

    public EstablishmentResponse findById(Long id){
        Establishment establishmentFound = findEstablishmentByIdOrThrow(id);
        return mapper.toDto(establishmentFound);
    }

    public List<EstablishmentResponse> findAll(){
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional
    public EstablishmentResponse update(Long id, EstablishmentUpdateDTO request){
        Establishment establishmentExists = findEstablishmentByIdOrThrow(id);

        if(request.cnpj() != null){
            if(!validateCnpjForUpdate(request.cnpj(), establishmentExists))
                throw new EntityAlreadyRegisteredException("CNPJ: " + request.cnpj() + " already registered!");
        }

        establishmentExists.setCnpj(UpdateHelper.getIfNotNull(request.cnpj(), establishmentExists.getCnpj()));
        establishmentExists.setName(UpdateHelper.getIfNotNull(request.name(), establishmentExists.getName()));
        establishmentExists.setPhone(UpdateHelper.getIfNotNull(request.phone(), establishmentExists.getPhone()));

        if(request.address() != null){
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
        return mapper.toDto(savedEstablishment);
    }

    @Transactional
    public void delete(Long id){
        Establishment establishmentExists = findEstablishmentByIdOrThrow(id);
        repository.delete(establishmentExists);
    }
}
