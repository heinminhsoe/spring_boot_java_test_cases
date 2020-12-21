package com.hms.mds.oms.controller;

import com.hms.mds.oms.controller.api.ProductAPI;
import com.hms.mds.oms.dto.ProductDto;
import com.hms.mds.oms.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController implements ProductAPI {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @Override
    public ResponseEntity<List<ProductDto>> findAll() {
        return new ResponseEntity<>(
                productService.findAll(),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<ProductDto> findById(Integer id) {
        return new ResponseEntity<>(
                productService.findById(id),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<ProductDto> create(ProductDto product) {
        return new ResponseEntity<>(
                productService.save(product),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<ProductDto> update(Integer id, ProductDto product) {
        return new ResponseEntity<>(
                productService.update(id, product),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<Void> delete(Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
