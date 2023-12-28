package com.ecommerce.api.eshopper.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String story;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
//    @JsonIgnore
    @JsonIgnoreProperties({"inventory", "summary", "hot", "active", "categories", "author"})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Product> products;

    @PreRemove
    public void preRemove() {
        for(Product product : products) {
            product.setAuthor(null);
        }
    }
    
}
