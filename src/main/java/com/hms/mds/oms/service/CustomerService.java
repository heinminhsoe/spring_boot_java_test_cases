package com.hms.mds.oms.service;

import com.hms.mds.oms.dto.CustomerDto;

import java.util.List;

public interface CustomerService {

    List<CustomerDto> findAll();

    CustomerDto findById(Integer id);

    CustomerDto save(CustomerDto customer);

    CustomerDto update(Integer id, CustomerDto customer);

    void delete(Integer id);
}
