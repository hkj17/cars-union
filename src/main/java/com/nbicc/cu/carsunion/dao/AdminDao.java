package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bigmao on 2017/8/18.
 */
@Repository
public interface AdminDao extends JpaRepository{

    Admin findByUserName(String userName);

}
