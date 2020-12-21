package com.hms.mds.oms.mapper;
import com.hms.mds.oms.dto.OrderItemsDto;
import com.hms.mds.oms.entity.OrderItems;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderItemsMapper {
    public OrderItems fromDto(OrderItemsDto dto) {
        return OrderItems.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .total(dto.getTotal())
                .build();
    }

    public OrderItemsDto toDto(OrderItems entity) {

        OrderItemsDto orderItemsDto = OrderItemsDto.builder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .total(entity.getTotal())
                .build();

        if(entity.getProduct() != null){
            orderItemsDto.setProductId(entity.getProduct().getId());
            orderItemsDto.setProductName(entity.getProduct().getName());
        }

        return orderItemsDto;
    }

    public List<OrderItemsDto> toDtos(Iterable<OrderItems> entityList) {
        List<OrderItemsDto> orderItemsDtos = new ArrayList<>();
        entityList.forEach(
                (OrderItems) -> orderItemsDtos.add(toDto(OrderItems))
        );
        return orderItemsDtos;
    }
}
