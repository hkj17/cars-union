package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.ProductClassDao;
import com.nbicc.cu.carsunion.dao.ProductDao;
import com.nbicc.cu.carsunion.model.Product;
import com.nbicc.cu.carsunion.model.ProductClass;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by bigmao on 2017/8/18.
 */
@Service
public class ProductService {
    @Autowired
    private ProductClassDao productClassDao;
    @Autowired
    private ProductDao productDao;

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
    public void deleteProductClass(String id, String path) {
        productClassDao.deleteById(id);
        if(CommonUtil.isNullOrEmpty(path)){
            productClassDao.deleteByPathLike(id + "%");
        }else{
            productClassDao.deleteByPathLike(path+","+id+"%");
        }
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

    public String addProduct(String classId, String name, String price, String specification, String feature) {
        ProductClass productClass = productClassDao.getById(classId);
        if(CommonUtil.isNullOrEmpty(productClass)){
            return "product class wrong.";
        }
        String id = UUID.randomUUID().toString().replace("-","");
        Product product = new Product(id,productClass,name,new BigDecimal(price),specification,feature,new Date(),"admin");
        productDao.save(product);
        return "ok";
    }

    public String editProduct(String productId, String classId, String name, String price, String specification, String feature) {
        Product product = productDao.getOne(productId);
        ProductClass productClass = productClassDao.getById(classId);
        if(CommonUtil.isNullOrEmpty(productClass)){
            return "product class wrong.";
        }else{
            product.setProductClass(productClass);
        }
        product.setName(name);
        product.setPrice(new BigDecimal(price));
        product.setSpecification(specification);
        product.setFeature(feature);
        productDao.save(product);
        return "ok";
    }

    public List<Product> getProductByClassId(String classId) {
        if(CommonUtil.isNullOrEmpty(classId)){
            return productDao.findAll();
        }else{
            return productDao.findByProductClass_Id(classId);
        }
    }

    public void deleteProduct(String productId) {
        productDao.delete(productId);
    }

    public Product getProductById(String id) {
        Product product = productDao.findOne(id);
        return product;
    }
}
