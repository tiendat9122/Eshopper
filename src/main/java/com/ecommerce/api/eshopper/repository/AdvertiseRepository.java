package com.ecommerce.api.eshopper.repository;

import com.ecommerce.api.eshopper.entity.Advertise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertiseRepository extends JpaRepository<Advertise, Long> {

    List<Advertise> findByActiveTrue();

}
