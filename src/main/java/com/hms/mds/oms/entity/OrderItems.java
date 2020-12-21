package com.hms.mds.oms.entity;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer quantity;
    private Double total;

    @ToString.Exclude
    @ManyToOne
    private Order order;

    @ToString.Exclude
    @ManyToOne
    private Product product;
}

