package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bigmao on 2017/8/18.
 */
public interface MerchantDao extends JpaRepository<Merchant,String> {

    Merchant findById(String id);

    Merchant findByContact(String contact);

    List<Merchant> findByRegStatus(int i);

    List<Merchant> findByRegionLike(String region);

    List<Merchant> findByNameLike(String name);
}
