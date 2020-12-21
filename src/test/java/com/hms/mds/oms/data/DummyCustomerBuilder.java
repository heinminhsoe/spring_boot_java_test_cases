package com.hms.mds.oms.data;

import com.hms.mds.oms.dto.CustomerDto;
import com.hms.mds.oms.entity.Customer;

import java.util.Arrays;
import java.util.List;

public class DummyCustomerBuilder {
    public static CustomerDto getCustomerDto(){
        return CustomerDto.builder()
                .name("Fribarg")
                .email("fribarg@gmail.com")
                .phone("+9594593949")
                .address("Yangon")
                .build();
    }

    public static Customer getCustomerEntity(){
        return Customer.builder()
                .name("Fribarg")
                .email("fribarg@gmail.com")
                .phone("+9594593949")
                .address("Yangon")
                .build();
    }

    public static List<Customer> getCustomerEntities(){
        Customer customer1 = getCustomerEntity();

        Customer customer2 = getCustomerEntity();
        customer2.setName("U Thant");
        customer2.setAddress("Mandalay");

        return Arrays.asList(customer1, customer2);
    }
}
