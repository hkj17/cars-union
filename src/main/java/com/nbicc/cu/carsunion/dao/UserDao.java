package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserDao extends JpaRepository<User, String> {

    User findById(String id);

    User findByContact(String contact);

    User findByShareCode(String shareCode);

    @Query("SELECT shareCode FROM User where shareCode is not null")
    List<String> findAllShareCode();
}
