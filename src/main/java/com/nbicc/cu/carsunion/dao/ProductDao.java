package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Product;
import com.nbicc.cu.carsunion.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by bigmao on 2017/8/21.
 */
public interface ProductDao extends JpaRepository<Product,String>,PagingAndSortingRepository<Product,String> {

    List<Product> findByClassIdLike(String classId);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findByClassIdLike(String classId, Pageable pageable);

    @Query("select vpr.product from VehicleProductRelationship vpr where vpr.vehicle = ?1 and vpr.product.classId like ?2")
    Page<Product> findByClassIdAndVehicle(Vehicle vehicle, String classId, Pageable pageable);


}
