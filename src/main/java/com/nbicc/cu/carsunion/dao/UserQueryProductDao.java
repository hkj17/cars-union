package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.UserQueryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserQueryProductDao extends JpaRepository<UserQueryProduct,Long>, PagingAndSortingRepository<UserQueryProduct,Long> {
}
