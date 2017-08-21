package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.ProductClassDao;
import com.nbicc.cu.carsunion.model.ProductClass;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by bigmao on 2017/8/18.
 */
@Service
public class ProductService {
    @Autowired
    private ProductClassDao productClassDao;

    public boolean addProductClass(String pid, String path, String name, Integer level) {
        String id = CommonUtil.generateUUID16();
        String currPath = "";
        if (!CommonUtil.isNullOrEmpty(pid)) {
            currPath = CommonUtil.isNullOrEmpty(path) ? "" : path + ",";
            currPath += pid;
        }
        int currLevel = level == null ? 0: level + 1;
        ProductClass productClass = new ProductClass(id,currPath,currLevel,name);
        productClassDao.save(productClass);
        return true;
    }

    @Transactional
    public boolean deleteProductClass(String id, String path) {
        productClassDao.deleteById(id);
        productClassDao.deleteByPathLike(path+","+id+"%");
        return true;
    }


    public List<ProductClass> getProductClass(String id, String path) {
        if(CommonUtil.isNullOrEmpty(id)){
            //根节点查询
            return productClassDao.findByLevel(0);
        }else if(CommonUtil.isNullOrEmpty(path)){
            return productClassDao.findByPath(id);
        }else{
            return productClassDao.findByPath(path + "," + id);
        }
    }
}
