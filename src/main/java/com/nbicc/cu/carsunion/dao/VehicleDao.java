package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleDao extends JpaRepository<Vehicle, String>{

    Vehicle findById(String id);

    List<Vehicle> findByPathOrderByNameAsc(String path);

    List<Vehicle> findByPathStartingWith(String path);

    @Query("select v from Vehicle v where v.level = 0 order by v.pinyin")
    List<Vehicle> findRootVehicles();

    @Query("select v.name from Vehicle v where v.id in ?1 order by v.level")
    List<String> findVehicleFullName(List<String> idList);

    List<Vehicle> findByLevelOrderByName(int level);
}
