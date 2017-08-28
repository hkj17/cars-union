package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenDao extends JpaRepository<Token,String> {

    public Token findById(String id);

    public Token findByToken(String token);
}
