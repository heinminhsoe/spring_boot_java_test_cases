package com.hms.mds.oms.mapper;

import com.hms.mds.oms.dto.OrderDto;
import com.hms.mds.oms.entity.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {
    public Order fromDto(OrderDto dto) {
        return Order.builder()
                .id(dto.getId())
                .orderNo(dto.getOrderNo())
                .orderDate(dto.getOrderDate())
                .status(dto.getStatus())
                .build();
    }

    public OrderDto toDto(Order entity) {
        OrderDto orderDto = OrderDto.builder()
                .id(entity.getId())
                .orderNo(entity.getOrderNo())
                .orderDate(entity.getOrderDate())
                .status(entity.getStatus())
                .build();

        if(entity.getCustomer() != null){
            orderDto.setCustomerId(entity.getCustomer().getId());
            orderDto.setCustomerName(entity.getCustomer().getName());
        }

        return orderDto;
    }

    public List<OrderDto> toDtos(Iterable<Order> entityList) {
        List<OrderDto> orderDtos = new ArrayList<>();
        entityList.forEach(
                (Order) -> orderDtos.add(toDto(Order))
        );
        return orderDtos;
    }
}
