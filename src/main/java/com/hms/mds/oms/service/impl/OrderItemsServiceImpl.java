package com.hms.mds.oms.service.impl;

import com.hms.mds.oms.dto.OrderItemsDto;
import com.hms.mds.oms.entity.Order;
import com.hms.mds.oms.entity.OrderItems;
import com.hms.mds.oms.entity.Product;
import com.hms.mds.oms.exception.EntityNotFoundException;
import com.hms.mds.oms.mapper.OrderItemsMapper;
import com.hms.mds.oms.repository.OrderItemsRepository;
import com.hms.mds.oms.repository.OrderRepository;
import com.hms.mds.oms.repository.ProductRepository;
import com.hms.mds.oms.service.OrderItemsService;
import com.hms.mds.oms.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderItemsServiceImpl implements OrderItemsService {

    private final OrderItemsMapper orderItemsMapper;
    private final OrderItemsRepository orderItemsRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderItemsServiceImpl(OrderItemsMapper orderItemsMapper, OrderItemsRepository orderItemsRepository,
                                 OrderRepository orderRepository, ProductRepository productRepository){
        this.orderItemsMapper = orderItemsMapper;
        this.orderItemsRepository = orderItemsRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderItemsDto save(Integer orderId, OrderItemsDto orderItemsDto) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_ORDER_NOT_FOUND)
                );

        Product product = productRepository.findById(orderItemsDto.getProductId())
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_PRODUCT_NOT_FOUND)
                );


        OrderItems orderItems = orderItemsMapper.fromDto(orderItemsDto);
        orderItems.setOrder(order);
        orderItems.setProduct(product);

        orderItems = orderItemsRepository.save(orderItems);
        return orderItemsMapper.toDto(orderItems);
    }

    @Override
    public OrderItemsDto update(Integer orderId, Integer id, OrderItemsDto orderItemsDto) {
        OrderItems orderItems = orderItemsRepository.findByOrderIdAndId(orderId, id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_ORDER_ITEMS_NOT_FOUND)
                );

        Product product = productRepository.findById(orderItemsDto.getProductId())
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_PRODUCT_NOT_FOUND)
                );

        OrderItems newOrderItems = orderItems.toBuilder()
                .quantity(orderItemsDto.getQuantity())
                .total(orderItemsDto.getTotal())
                .build();

        newOrderItems.setOrder(orderItems.getOrder());
        newOrderItems.setProduct(product);

        orderItems = orderItemsRepository.save(newOrderItems);

        return orderItemsMapper.toDto(newOrderItems);
    }

    @Override
    public void delete(Integer orderId, Integer id) {
        OrderItems orderItems = orderItemsRepository.findByOrderIdAndId(orderId, id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_ORDER_ITEMS_NOT_FOUND)
                );

        orderItemsRepository.delete(orderItems);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemsDto> findAll(Integer orderId) {
        return orderItemsMapper.toDtos(
                orderItemsRepository.findByOrderId(orderId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemsDto findById(Integer orderId, Integer id) {
        OrderItems orderItems = orderItemsRepository.findByOrderIdAndId(orderId, id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_ORDER_ITEMS_NOT_FOUND)
                );
        return orderItemsMapper.toDto(orderItems);
    }
}