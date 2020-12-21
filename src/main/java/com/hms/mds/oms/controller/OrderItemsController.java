package com.hms.mds.oms.controller;


import com.hms.mds.oms.controller.api.OrderItemsAPI;
import com.hms.mds.oms.dto.OrderItemsDto;
import com.hms.mds.oms.service.OrderItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderItemsController implements OrderItemsAPI {

    private final OrderItemsService orderItemsService;

    public OrderItemsController(OrderItemsService orderItemsService){
        this.orderItemsService = orderItemsService;
    }

    @Override
    public ResponseEntity<List<OrderItemsDto>> findAll(Integer orderId) {
        return new ResponseEntity<>(
                orderItemsService.findAll(orderId),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<OrderItemsDto> findById(Integer orderId, Integer id) {
        return new ResponseEntity<>(
                orderItemsService.findById(orderId, id),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<OrderItemsDto> create(Integer orderId, OrderItemsDto orderItems) {
        return new ResponseEntity<>(
                orderItemsService.save(orderId, orderItems),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<OrderItemsDto> update(Integer orderId, Integer id, OrderItemsDto orderItems) {
        return new ResponseEntity<>(
                orderItemsService.update(orderId, id, orderItems),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<Void> delete(Integer orderId, Integer id) {
        orderItemsService.delete(orderId, id);
        return ResponseEntity.noContent().build();
    }
}

