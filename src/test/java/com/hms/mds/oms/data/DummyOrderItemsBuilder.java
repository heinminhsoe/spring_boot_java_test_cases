package com.hms.mds.oms.data;

import com.hms.mds.oms.dto.OrderItemsDto;
import com.hms.mds.oms.entity.OrderItems;

import java.util.Arrays;
import java.util.List;

public class DummyOrderItemsBuilder {
    public static OrderItemsDto getOrderItemsDto(){
        return OrderItemsDto.builder()
                .quantity(3)
                .total(10000D)
                .build();
    }

    public static OrderItems getOrderItemsEntity(){
        return OrderItems.builder()
                .quantity(3)
                .total(10000D)
                .build();
    }

    public static List<OrderItems> getOrderItemsEntities(){
        OrderItems orderItems1 = getOrderItemsEntity();
        OrderItems orderItems2 = getOrderItemsEntity();
        orderItems2.setTotal(20000D);

        return Arrays.asList(orderItems1, orderItems2);
    }
}
