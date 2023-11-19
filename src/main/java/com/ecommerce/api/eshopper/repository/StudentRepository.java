package com.ecommerce.api.eshopper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.api.eshopper.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
}
