package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Present;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PresentDao extends JpaRepository<Present,Long>,PagingAndSortingRepository<Present,Long> {
}
