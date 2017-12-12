package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.PresentDao;
import com.nbicc.cu.carsunion.model.Present;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PresentService {

    @Autowired
    private PresentDao presentDao;

    @Transactional
    public boolean addPresent(String name, String photo, int creditValue, int quantity, boolean onsale){
        Present present = new Present();
        present.setName(name);
        present.setPhoto(photo);
        present.setCreditValue(creditValue);
        present.setTotalQuantity(quantity);
        present.setOnSale(onsale);
        present.setDelFlag(false);
        presentDao.save(present);
        return true;
    }

    @Transactional
    public boolean deletePresent(long id){
        Present present = presentDao.findByIdAndDelFlag(id,false);
        if(CommonUtil.isNullOrEmpty(present))
            return false;
        present.setDelFlag(true);
        presentDao.save(present);
        return true;
    }

    @Transactional
    public boolean editPresent(long id,String name,String photo,int creditValue){
        Present present = presentDao.findByIdAndDelFlag(id,false);
        if(CommonUtil.isNullOrEmpty(present))
            return false;
        present.setName(name);
        present.setPhoto(photo);
        present.setCreditValue(creditValue);
        presentDao.save(present);
        return true;
    }

    public boolean editPresentOnSale(long id, boolean onSale){
        Present present = presentDao.findByIdAndDelFlag(id,false);
        if(CommonUtil.isNullOrEmpty(present))
            return false;
        present.setOnSale(onSale);
        presentDao.save(present);
        return true;
    }

    public Page<Present> getAllPresentList(int pageNum, int pageSize){
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        return presentDao.findByDelFlag(false,pageable);
    }

    public Page<Present> getOnSalePresentList(int pageNum, int pageSize){
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        return presentDao.findByDelFlagAndOnSale(false,true,pageable);
    }

    public Present getPresentById(long id){
        return presentDao.findByIdAndDelFlag(id,false);
    }
}
