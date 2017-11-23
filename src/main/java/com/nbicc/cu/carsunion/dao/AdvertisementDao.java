package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AdvertisementDao extends JpaRepository<Advertisement,Long>,PagingAndSortingRepository<Advertisement,Long> {

    List<Advertisement> findByIsShow(Integer isShow);

    Page<Advertisement> findByLocation(Integer location, Pageable pageable);

}
