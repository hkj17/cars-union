package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductTagDao extends JpaRepository<ProductTag,String> {

    List<ProductTag> findByProductClassLike(String productClassId);
}
