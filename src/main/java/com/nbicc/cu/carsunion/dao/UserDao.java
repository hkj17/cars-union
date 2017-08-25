package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserDao extends JpaRepository<User, String> {

    User findById(String id);

    User findByContact(String contact);
}
