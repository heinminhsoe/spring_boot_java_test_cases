package com.hms.mds.oms.service.impl;

import com.hms.mds.oms.dto.OrderDto;
import com.hms.mds.oms.entity.Customer;
import com.hms.mds.oms.entity.Order;
import com.hms.mds.oms.exception.EntityNotFoundException;
import com.hms.mds.oms.mapper.OrderMapper;
import com.hms.mds.oms.repository.CustomerRepository;
import com.hms.mds.oms.repository.OrderRepository;
import com.hms.mds.oms.service.OrderService;
import com.hms.mds.oms.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, OrderRepository orderRepository,
                            CustomerRepository customerRepository){
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public OrderDto save(OrderDto orderDto) {

        Order order = orderMapper.fromDto(orderDto);

        if(orderDto.getCustomerId() != null && orderDto.getCustomerId() > 0) {
            Customer customer = customerRepository.findById(orderDto.getCustomerId())
                    .orElseThrow(
                            () -> new EntityNotFoundException(Constants.MSG_CUSTOMER_NOT_FOUND)
                    );
            order.setCustomer(customer);
        }

        order =orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto update(Integer id, OrderDto orderDto) {

        Order order = orderRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_ORDER_NOT_FOUND)
                );

        order = order.toBuilder()
                .orderNo(orderDto.getOrderNo())
                .orderDate(orderDto.getOrderDate())
                .status(orderDto.getStatus())
                .build();

        if(orderDto.getCustomerId() != null && orderDto.getCustomerId() > 0) {
            Customer customer = customerRepository.findById(orderDto.getCustomerId())
                    .orElseThrow(
                            () -> new EntityNotFoundException(Constants.MSG_CUSTOMER_NOT_FOUND)
                    );
            order.setCustomer(customer);
        }else {
            order.setCustomer(null);
        }

        order = orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public void delete(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_ORDER_NOT_FOUND)
                );

        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> findAll() {
        return orderMapper.toDtos(
                orderRepository.findAll()
        );
    }

    @Override
    public OrderDto findById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_ORDER_NOT_FOUND)
                );
        return orderMapper.toDto(order);
    }
}