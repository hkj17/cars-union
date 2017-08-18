package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bigmao on 2017/8/18.
 */
public interface MerchantDao extends JpaRepository {

    Merchant findById(String id);
}
