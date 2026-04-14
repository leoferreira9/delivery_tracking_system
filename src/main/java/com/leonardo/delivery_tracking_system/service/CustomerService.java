package com.leonardo.delivery_tracking_system.service;

import com.leonardo.delivery_tracking_system.dto.address.AddressUpdateDTO;
import com.leonardo.delivery_tracking_system.dto.customer.CustomerRequest;
import com.leonardo.delivery_tracking_system.dto.customer.CustomerResponse;
import com.leonardo.delivery_tracking_system.dto.customer.CustomerUpdateDTO;
import com.leonardo.delivery_tracking_system.exception.EntityAlreadyRegisteredException;
import com.leonardo.delivery_tracking_system.exception.EntityNotFoundException;
import com.leonardo.delivery_tracking_system.mapper.CustomerMapper;
import com.leonardo.delivery_tracking_system.model.Address;
import com.leonardo.delivery_tracking_system.model.Customer;
import com.leonardo.delivery_tracking_system.repository.CustomerRepository;
import com.leonardo.delivery_tracking_system.utils.UpdateHelper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper){
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    private Customer findCustomerByIdOrThrow(Long id){
        return customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));
    }

    private boolean validateCpfAlreadyExists(String newCpf){
        return customerRepository.findByCpf(newCpf).isPresent();
    }

    private boolean validateEmailAlreadyExists(String email){
        return customerRepository.findByEmail(email).isPresent();
    }

    private boolean validateCpfForUpdate(String newCpf, Customer customerExists){
        Optional<Customer> customerFoundByCpf = customerRepository.findByCpf(newCpf);
        return customerFoundByCpf.isEmpty() || customerFoundByCpf.get().getId().equals(customerExists.getId());
    }

    private boolean validateEmailForUpdate(String newEmail, Customer customerExists){
        Optional<Customer> customerFoundByEmail = customerRepository.findByEmail(newEmail);
        return customerFoundByEmail.isEmpty() || customerFoundByEmail.get().getId().equals(customerExists.getId());
    }

    public CustomerResponse create(CustomerRequest request){

        if(validateCpfAlreadyExists(request.cpf()))
            throw new EntityAlreadyRegisteredException("CPF " + request.cpf() + " already registered!");

        if(validateEmailAlreadyExists(request.email()))
            throw new EntityAlreadyRegisteredException("Email " + request.email() + " already registered!");

        Customer customer = customerMapper.toEntity(request);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    private Address updateAddress(Customer customer, AddressUpdateDTO request){
        if(request != null){
            if(customer.getAddress() == null) {
                Address newAddress = new Address();

                newAddress.setStreet(request.street());
                newAddress.setNumber(request.number());
                newAddress.setZipCode(request.zipCode());
                newAddress.setCity(request.city());
                newAddress.setNeighborhood(request.neighborhood());
                customer.setAddress(newAddress);
            } else {
                Address customerAddress = customer.getAddress();
                customerAddress.setStreet(UpdateHelper.getIfNotNull(request.street(), customerAddress.getStreet()));
                customerAddress.setNumber(UpdateHelper.getIfNotNull(request.number(), customerAddress.getNumber()));
                customerAddress.setZipCode(UpdateHelper.getIfNotNull(request.zipCode(), customerAddress.getZipCode()));
                customerAddress.setCity(UpdateHelper.getIfNotNull(request.city(), customerAddress.getCity()));
                customerAddress.setNeighborhood(UpdateHelper.getIfNotNull(request.neighborhood(), customerAddress.getNeighborhood()));
            }
        }

        return customer.getAddress();
    }

    public CustomerResponse findById(Long id){
        Customer customerFound = findCustomerByIdOrThrow(id);
        return customerMapper.toDto(customerFound);
    }

    public List<CustomerResponse> findAll(){
        return customerRepository.findAll().stream().map(customerMapper::toDto).toList();
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerUpdateDTO request){
        Customer customerExists = findCustomerByIdOrThrow(id);

        if(request.cpf() != null){
            if(!validateCpfForUpdate(request.cpf(), customerExists))
                throw new EntityAlreadyRegisteredException("CPF " + request.cpf() + " already registered!");
        }

        if(request.email() != null){
            if(!validateEmailForUpdate(request.email(), customerExists))
                throw new EntityAlreadyRegisteredException("Email " + request.email() + " already registered!");
        }

        customerExists.setName(UpdateHelper.getIfNotNull(request.name(), customerExists.getName()));
        customerExists.setCpf(UpdateHelper.getIfNotNull(request.cpf(), customerExists.getCpf()));
        customerExists.setPhone(UpdateHelper.getIfNotNull(request.phone(), customerExists.getPhone()));
        customerExists.setEmail(UpdateHelper.getIfNotNull(request.email(), customerExists.getEmail()));

        Address address = updateAddress(customerExists, request.address());
        customerExists.setAddress(address);

        Customer savedCustomer = customerRepository.save(customerExists);

        return customerMapper.toDto(savedCustomer);
    }

    @Transactional
    public void delete(Long id){
        Customer customerExists = findCustomerByIdOrThrow(id);
        customerRepository.delete(customerExists);
    }
}
