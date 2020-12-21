package com.hms.mds.oms.service;

import com.hms.mds.oms.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAll();

    ProductDto findById(Integer id);

    ProductDto save(ProductDto product);

    ProductDto update(Integer id, ProductDto product);

    void delete(Integer id);
}
