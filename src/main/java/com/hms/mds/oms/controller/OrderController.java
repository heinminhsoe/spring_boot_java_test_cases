package com.hms.mds.oms.controller;


import com.hms.mds.oms.controller.api.OrderAPI;
import com.hms.mds.oms.dto.OrderDto;
import com.hms.mds.oms.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController implements OrderAPI {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<List<OrderDto>> findAll() {
        return new ResponseEntity<>(
                orderService.findAll(),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<OrderDto> findById(Integer id) {
        return new ResponseEntity<>(
                orderService.findById(id),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<OrderDto> create(OrderDto order) {
        return new ResponseEntity<>(
                orderService.save(order),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<OrderDto> update(Integer id, OrderDto order) {
        return new ResponseEntity<>(
                orderService.update(id, order),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<Void> delete(Integer id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

