package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bigmao on 2017/8/18.
 */
public interface AdminDao extends JpaRepository<Admin,String>{

    Admin findByUserName(String userName);

}
