package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Product;
import com.nbicc.cu.carsunion.model.Vehicle;
import com.nbicc.cu.carsunion.model.VehicleProductRelationship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by bigmao on 2017/8/24.
 */
public interface VehicleProductRelationshipDao extends JpaRepository<VehicleProductRelationship,Long>,PagingAndSortingRepository<VehicleProductRelationship,Long>{

    VehicleProductRelationship findByVehicleAndProduct(Vehicle vehicle, Product product);

    List<VehicleProductRelationship> findByProduct(Product product);

    Page<VehicleProductRelationship> findByVehicle(Vehicle vehicle, Pageable pageable);

}
