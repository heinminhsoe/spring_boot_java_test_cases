package com.hms.mds.oms.repository;

import com.hms.mds.oms.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {

    List<OrderItems> findByOrderId(Integer orderId);

    Optional<OrderItems> findByOrderIdAndId(Integer orderId, Integer id);
}
