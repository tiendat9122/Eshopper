package com.ecommerce.api.eshopper.repository;

import com.ecommerce.api.eshopper.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    List<Product> findByNameContaining(String name);

    List<Product> findByAuthorName(String name);

    List<Product> findByActiveTrue();

    @Query("SELECT p FROM Product p " +
           "JOIN p.author a " +
           "WHERE p.active = true " +
           "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p ORDER BY RAND() LIMIT 5")
    List<Product> findRandomProducts();
    
}
