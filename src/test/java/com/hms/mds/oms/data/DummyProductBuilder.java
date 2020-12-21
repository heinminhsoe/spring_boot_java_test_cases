package com.hms.mds.oms.data;

import com.hms.mds.oms.dto.ProductDto;
import com.hms.mds.oms.entity.Product;

import java.util.Arrays;
import java.util.List;

public class DummyProductBuilder {
    public static ProductDto getProductDto(){
        return ProductDto.builder()
                .name("iPhone")
                .unitPrice(10000D)
                .build();
    }

    public static Product getProductEntity(){
        return Product.builder()
                .name("iPhone")
                .unitPrice(10000D)
                .build();
    }

    public static List<Product> getProductEntities(){
        Product product1 = getProductEntity();
        Product product2 = getProductEntity();
        product2.setName("Glaxy Note 10");

        return Arrays.asList(product1, product2);
    }
}
