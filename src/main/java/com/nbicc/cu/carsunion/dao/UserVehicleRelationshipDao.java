package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.User;
import com.nbicc.cu.carsunion.model.UserVehicleRelationship;
import com.nbicc.cu.carsunion.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserVehicleRelationshipDao extends JpaRepository<UserVehicleRelationship, Long>{
    UserVehicleRelationship findByUserAndVehicleAndPlateNum(User user, Vehicle vehicle,String plateNum);

    List<UserVehicleRelationship> findByUser(User u);

    UserVehicleRelationship findByUserAndIsDefault(User u, Boolean isDefault);

}
