package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementDao extends JpaRepository<Advertisement,Long>{

    List<Advertisement> findByIsShow(Integer isShow);

}
