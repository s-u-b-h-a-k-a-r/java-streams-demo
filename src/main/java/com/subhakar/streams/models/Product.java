package com.subhakar.streams.models;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;

    @With
    private Double price;

    @ManyToMany(mappedBy = "products")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Order> orders;
}
