package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Product;
import com.nbicc.cu.carsunion.model.Vehicle;
import com.nbicc.cu.carsunion.model.VehicleProductRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bigmao on 2017/8/24.
 */
public interface VehicleProductRelationshipDao extends JpaRepository<VehicleProductRelationship,Long>{

    VehicleProductRelationship findByVehicleAndProduct(Vehicle vehicle, Product product);
}
