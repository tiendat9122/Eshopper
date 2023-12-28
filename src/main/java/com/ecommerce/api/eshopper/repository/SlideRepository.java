package com.ecommerce.api.eshopper.repository;

import com.ecommerce.api.eshopper.entity.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Long> {

    List<Slide> findByActiveTrue();

}
