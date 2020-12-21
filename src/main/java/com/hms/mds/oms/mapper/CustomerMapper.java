package com.hms.mds.oms.mapper;

import com.hms.mds.oms.dto.CustomerDto;
import com.hms.mds.oms.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerMapper {
    public Customer fromDto(CustomerDto dto) {
        return Customer.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
    }

    public CustomerDto toDto(Customer entity) {
        return CustomerDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .build();
    }

    public List<CustomerDto> toDtos(Iterable<Customer> entityList) {
        List<CustomerDto> customerDtos = new ArrayList<>();
        entityList.forEach(
                (Customer) -> customerDtos.add(toDto(Customer))
        );
        return customerDtos;
    }
}
