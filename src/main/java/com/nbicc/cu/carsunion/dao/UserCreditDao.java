package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.UserCredit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCreditDao extends JpaRepository<UserCredit,String>{

    UserCredit findByUserId(String id);
}
