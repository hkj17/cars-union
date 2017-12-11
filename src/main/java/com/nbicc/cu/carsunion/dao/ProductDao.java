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

    Product findByIdAndDelFlag(String id,int delFlag);

    Product findById(String id);

    List<Product> findByClassIdLikeAndDelFlag(String classId,int delFlag);

    List<Product> findByGroupMarkAndDelFlag(String groupMark,int delFlag);

//    Page<Product> findAllByDelFlag(int delFlag, Pageable pageable);

    Page<Product> findByClassIdLikeAndNameLikeAndOnSaleAndDelFlag(String classId,String name, int onSale, int delFlag, Pageable pageable);

    @Query("select vpr.product from VehicleProductRelationship vpr where vpr.vehicle = ?1 and vpr.product.classId like ?2 and vpr.product.name like ?3 and vpr.product.delFlag = 0 and vpr.product.onSale = ?4")
    Page<Product> findByClassIdAndVehicleAndSearchStr(Vehicle vehicle, String classId,String searchStr, int onSale, Pageable pageable);

    Page<Product> findAllByNameLikeAndOnSaleAndDelFlag(String name, int onSale, int delFlag, Pageable pageable);
}
