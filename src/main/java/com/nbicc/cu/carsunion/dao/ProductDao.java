package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by bigmao on 2017/8/21.
 */
public interface ProductDao extends JpaRepository<Product,String>{

    Product findById(String id);

    List<Product> findByClassIdLike(String classId);

}
