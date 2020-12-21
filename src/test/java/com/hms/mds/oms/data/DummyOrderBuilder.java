package com.hms.mds.oms.data;

import com.hms.mds.oms.dto.OrderDto;
import com.hms.mds.oms.entity.Order;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class DummyOrderBuilder {
    public static OrderDto getOrderDto(){
        return OrderDto.builder()
                .orderNo(1001L)
                .orderDate(LocalDate.now())
                .status("PENDING")
                .build();
    }

    public static Order getOrderEntity(){
        return Order.builder()
                .orderNo(1001L)
                .orderDate(LocalDate.now())
                .status("PENDING")
                .build();
    }

    public static List<Order> getOrderEntities(){
        Order order1 = getOrderEntity();
        Order order2 = getOrderEntity();
        order2.setOrderNo(1002L);

        return Arrays.asList(order1, order2);
    }
}
