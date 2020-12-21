package com.hms.mds.oms.repository;

import com.hms.mds.oms.entity.Order;
import com.hms.mds.oms.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByCustomerIdAndId(Integer customerId, Integer id);
}
