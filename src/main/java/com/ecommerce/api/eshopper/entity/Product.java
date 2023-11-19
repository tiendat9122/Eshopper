package com.ecommerce.api.eshopper.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double retail;

    private Long inventory;

    private String picture;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = true)
    @JsonIgnoreProperties("products")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Author author;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @JsonIgnoreProperties("products")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Category> categories;

    @PreRemove
    public void preRemove() {
        for (Category category : categories) {
            category.getProducts().remove(this);
        }
    }

}
