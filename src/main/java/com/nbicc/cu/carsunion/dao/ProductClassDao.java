package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.ProductClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by bigmao on 2017/8/18.
 */
public interface ProductClassDao extends JpaRepository<ProductClass,String>{

    void deleteById(String id);

    List<ProductClass> findByLevel(int level);

    List<ProductClass> findByPath(String path);

    @Modifying
    @Transactional
    @Query("delete from ProductClass p where p.path like ?1")
    void deleteByPathLike(String path);

}
