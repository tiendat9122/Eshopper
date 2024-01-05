package com.ecommerce.api.eshopper.repository;

import com.ecommerce.api.eshopper.dto.ProductQuantityDto;
import com.ecommerce.api.eshopper.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Product> findByKeyword(@Param("keyword") String keyword, Pageable page);

    @Query("SELECT p FROM Product p ORDER BY RAND() LIMIT 5")
    List<Product> findRandomProducts();

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.hot = true ORDER BY p.id DESC LIMIT 8")
    List<Product> findHotActiveProductsLimitedTo8();

    List<Product> findByCategoriesId(Long categoryId);

    //Query list Product have the most of quantity amout sold
    @Query(value = "SELECT new com.ecommerce.api.eshopper.dto.ProductQuantityDto(p, SUM(od.quantity) as totalQuantity) " +
            "FROM Product p " +
            "JOIN OrderDetail od ON p.id = od.product.id " +
            "JOIN od.orders o " +
            "WHERE o.state = true " +
            "GROUP BY p " +
            "ORDER BY totalQuantity DESC LIMIT 8")
//    List<ProductQuantityDto> findProductsByMaxQuantity();
    List<ProductQuantityDto> findTopProductsByMaxQuantityWithLimit8();

    @Query("SELECT p FROM Product p INNER JOIN p.categories c " +
            "WHERE c.id = :categoryId AND LOWER(p.name) LIKE %:productName% AND p.active = true")
    Page<Product> findByCategoryIdAndProductName(
            @Param("categoryId") Long categoryId,
            @Param("productName") String productName,
            Pageable pageable
    );

    @Query("SELECT p FROM Product p INNER JOIN p.author a " +
            "WHERE a.id = :authorId AND LOWER(p.name) LIKE %:productName% AND p.active = true")
    Page<Product> findByAuthorIdAndProductName(
            @Param("authorId") Long authorId,
            @Param("productName") String productName,
            Pageable pageable
    );

}
