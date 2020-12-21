package com.hms.mds.oms.controller;


import com.hms.mds.oms.controller.api.CustomerAPI;
import com.hms.mds.oms.dto.CustomerDto;
import com.hms.mds.oms.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController implements CustomerAPI {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @Override
    public ResponseEntity<List<CustomerDto>> findAll() {
        return new ResponseEntity<>(
                customerService.findAll(),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<CustomerDto> findById(Integer id) {
        return new ResponseEntity<>(
                customerService.findById(id),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<CustomerDto> create(CustomerDto customer) {
        return new ResponseEntity<>(
                customerService.save(customer),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<CustomerDto> update(Integer id, CustomerDto customer) {
        return new ResponseEntity<>(
                customerService.update(id, customer),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<Void> delete(Integer id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

