package com.subhakar.streams.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_date")
    private LocalDate orderDate;
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "order_product_relationship",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")}
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Product> products;
}
