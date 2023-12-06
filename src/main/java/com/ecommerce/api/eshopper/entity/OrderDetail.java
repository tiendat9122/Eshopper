package com.ecommerce.api.eshopper.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "OrderDetail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;

    private double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = true)
    @JsonIgnoreProperties({"inventory", "author", "categories"})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orders_id", nullable = true)
    @JsonIgnoreProperties({"orderDate", "note", "user"})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Orders orders;

}
