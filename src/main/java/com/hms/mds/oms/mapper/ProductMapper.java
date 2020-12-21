package com.hms.mds.oms.mapper;

import com.hms.mds.oms.dto.ProductDto;
import com.hms.mds.oms.entity.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {

    public Product fromDto(ProductDto dto) {
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .unitPrice(dto.getUnitPrice())
                .build();
    }

    public ProductDto toDto(Product entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .unitPrice(entity.getUnitPrice())
                .build();
    }

    public List<ProductDto> toDtos(Iterable<Product> entityList) {
        List<ProductDto> productDtos = new ArrayList<>();
        entityList.forEach(
                (product) -> productDtos.add(toDto(product))
        );
        return productDtos;
    }
}