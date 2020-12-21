package com.hms.mds.oms.repository;

import com.hms.mds.oms.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
