package com.ecommerce.api.eshopper.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.api.eshopper.entity.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>{
    
    Page<Feedback> findByProduct_Id(Long productId, Pageable page);
    
}
