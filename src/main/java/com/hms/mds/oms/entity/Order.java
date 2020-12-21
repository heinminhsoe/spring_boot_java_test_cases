package com.hms.mds.oms.entity;


import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Long orderNo;
    private LocalDate orderDate;
    private String status;

    @ToString.Exclude
    @ManyToOne
    private Customer customer;

}

