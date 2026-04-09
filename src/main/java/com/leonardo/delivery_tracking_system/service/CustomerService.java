package com.leonardo.delivery_tracking_system.service;

import com.leonardo.delivery_tracking_system.dto.customer.CustomerRequest;
import com.leonardo.delivery_tracking_system.dto.customer.CustomerResponse;
import com.leonardo.delivery_tracking_system.dto.customer.CustomerUpdateDTO;
import com.leonardo.delivery_tracking_system.exception.EntityAlreadyRegisteredException;
import com.leonardo.delivery_tracking_system.exception.EntityNotFoundException;
import com.leonardo.delivery_tracking_system.mapper.CustomerMapper;
import com.leonardo.delivery_tracking_system.model.Address;
import com.leonardo.delivery_tracking_system.model.Customer;
import com.leonardo.delivery_tracking_system.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper){
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public Customer findCustomerByIdOrThrow(Long id){
        return customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));
    }

    private <T> T getIfNotNull(T newValue, T currentValue){
        return newValue != null ? newValue : currentValue;
    }

    public CustomerResponse create(CustomerRequest request){
        Customer customer = customerMapper.toEntity(request);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    public CustomerResponse findById(Long id){
        Customer customerFound = findCustomerByIdOrThrow(id);
        return customerMapper.toDto(customerFound);
    }

    public List<CustomerResponse> findAll(){
        return customerRepository.findAll().stream().map(customerMapper::toDto).toList();
    }

    public CustomerResponse update(Long id, CustomerUpdateDTO request){
        Customer customerExists = findCustomerByIdOrThrow(id);

        if(request.cpf() != null && !request.cpf().equals(customerExists.getCpf())){
            if(customerRepository.findByCpf(request.cpf()).isPresent())
                throw new EntityAlreadyRegisteredException("CPF " + request.cpf() + " already registered!");
        }

        if(request.email() != null && !request.email().equals(customerExists.getEmail())){
            if(customerRepository.findByEmail(request.email()).isPresent())
                throw new EntityAlreadyRegisteredException("Email " + request.email() + " already registered!");
        }

        customerExists.setName(getIfNotNull(request.name(), customerExists.getName()));
        customerExists.setCpf(getIfNotNull(request.cpf(), customerExists.getCpf()));
        customerExists.setPhone(getIfNotNull(request.phone(), customerExists.getPhone()));
        customerExists.setEmail(getIfNotNull(request.email(), customerExists.getEmail()));

        if(request.address() != null){
            if(customerExists.getAddress() == null) {
                Address newAddress = new Address();
                newAddress.setStreet(request.address().street());
                newAddress.setNumber(request.address().number());
                newAddress.setZipCode(request.address().zipCode());
                newAddress.setCity(request.address().city());
                newAddress.setNeighborhood(request.address().neighborhood());
                customerExists.setAddress(newAddress);
            } else {
                Address customerAddress = customerExists.getAddress();
                customerAddress.setStreet(getIfNotNull(request.address().street(), customerAddress.getStreet()));
                customerAddress.setNumber(getIfNotNull(request.address().number(), customerAddress.getNumber()));
                customerAddress.setZipCode(getIfNotNull(request.address().zipCode(), customerAddress.getZipCode()));
                customerAddress.setCity(getIfNotNull(request.address().city(), customerAddress.getCity()));
                customerAddress.setNeighborhood(getIfNotNull(request.address().neighborhood(), customerAddress.getNeighborhood()));
            }
        }

        Customer savedCustomer = customerRepository.save(customerExists);

        return customerMapper.toDto(savedCustomer);
    }

    public void delete(Long id){
        Customer customerExists = findCustomerByIdOrThrow(id);
        customerRepository.delete(customerExists);
    }
}
