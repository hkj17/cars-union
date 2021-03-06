package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Address;
import com.nbicc.cu.carsunion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressDao extends JpaRepository<Address, String> {
    public Address findById(String id);

    public List<Address> findByUser(User u);

    public Address findByUserAndIsDefault(User u, Boolean isDefault);
}
