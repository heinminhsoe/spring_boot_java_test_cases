package com.hms.mds.oms.service.impl;

import com.hms.mds.oms.dto.ProductDto;
import com.hms.mds.oms.entity.Product;
import com.hms.mds.oms.exception.EntityNotFoundException;
import com.hms.mds.oms.mapper.ProductMapper;
import com.hms.mds.oms.repository.ProductRepository;
import com.hms.mds.oms.service.ProductService;
import com.hms.mds.oms.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper, ProductRepository productRepository){
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        Product product = productRepository.save(
                productMapper.fromDto(productDto)
        );
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto update(Integer id, ProductDto productDto) {

        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_PRODUCT_NOT_FOUND)
                );

        product = product.toBuilder()
                .name(productDto.getName())
                .unitPrice(productDto.getUnitPrice())
                .build();

        product = productRepository.save(product);

        return productMapper.toDto(product);
    }

    @Override
    public void delete(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_PRODUCT_NOT_FOUND)
                );

        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> findAll() {
        return productMapper.toDtos(
                productRepository.findAll()
        );
    }

    @Override
    public ProductDto findById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Constants.MSG_PRODUCT_NOT_FOUND)
                );
        return productMapper.toDto(product);
    }
}
