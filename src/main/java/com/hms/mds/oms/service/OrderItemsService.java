package com.hms.mds.oms.service;

import com.hms.mds.oms.dto.OrderItemsDto;

import java.util.List;

public interface OrderItemsService {

    List<OrderItemsDto> findAll(Integer orderId);

    OrderItemsDto findById(Integer orderId, Integer id);

    OrderItemsDto save(Integer orderId, OrderItemsDto orderItems);

    OrderItemsDto update(Integer orderId, Integer id, OrderItemsDto orderItems);

    void delete(Integer orderId, Integer id);
}
