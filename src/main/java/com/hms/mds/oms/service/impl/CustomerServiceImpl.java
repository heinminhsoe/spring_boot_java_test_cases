package com.hms.mds.oms.service.impl;

import com.hms.mds.oms.dto.CustomerDto;
import com.hms.mds.oms.entity.Customer;
import com.hms.mds.oms.exception.EntityNotFoundException;
import com.hms.mds.oms.mapper.CustomerMapper;
import com.hms.mds.oms.repository.CustomerRepository;
import com.hms.mds.oms.service.CustomerService;
import com.hms.mds.oms.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepository customerRepository){
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDto save(CustomerDto customerDto) {
        Customer customer = customerRepository.save(
                customerMapper.fromDto(customerDto)
        );
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDto update(Integer id, CustomerDto customerDto) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_CUSTOMER_NOT_FOUND)
                );

        customer = customer.toBuilder()
                .name(customerDto.getName())
                .email(customerDto.getEmail())
                .phone(customerDto.getPhone())
                .address(customerDto.getAddress())
                .build();

        customer = customerRepository.save(customer);

        return customerMapper.toDto(customer);
    }

    @Override
    public void delete(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_CUSTOMER_NOT_FOUND)
                );

        customerRepository.delete(customer);
    }

    @Override
    public List<CustomerDto> findAll() {
        return customerMapper.toDtos(
                customerRepository.findAll()
        );
    }

    @Override
    public CustomerDto findById(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_CUSTOMER_NOT_FOUND)
                );
        return customerMapper.toDto(customer);
    }
}