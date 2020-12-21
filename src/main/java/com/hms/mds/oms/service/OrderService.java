package com.hms.mds.oms.service;

import com.hms.mds.oms.dto.OrderDto;

import java.util.List;

public interface OrderService {

    List<OrderDto> findAll();

    OrderDto findById(Integer id);

    OrderDto save(OrderDto order);

    OrderDto update(Integer id, OrderDto order);

    void delete(Integer id);
}