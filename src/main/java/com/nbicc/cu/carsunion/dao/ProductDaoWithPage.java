package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by bigmao on 2017/9/20.
 */
public interface ProductDaoWithPage extends PagingAndSortingRepository<Product,String> {

    Page<Product> findAll(Pageable pageable);

    Page<Product> findByClassIdLike(String classId, Pageable pageable);
}
