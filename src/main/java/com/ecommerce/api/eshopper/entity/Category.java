package com.ecommerce.api.eshopper.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "categories", cascade = { CascadeType.PERSIST, CascadeType.REFRESH,
            CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("categories")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Product> products;

    @PreRemove
    public void preRemove() {
        for (Product product : products) {
            product.getCategories().remove(this);
        }
    }

}
