package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.*;
import com.nbicc.cu.carsunion.model.Product;
import com.nbicc.cu.carsunion.model.ProductClass;
import com.nbicc.cu.carsunion.model.Vehicle;
import com.nbicc.cu.carsunion.model.VehicleProductRelationship;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.QiniuUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bigmao on 2017/8/18.
 */
@Service
public class ProductService {

    private Logger logger = Logger.getLogger(ProductService.class);

    @Autowired
    private ProductClassDao productClassDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private VehicleProductRelationshipDao vehicleProductRelationshipDao;

    public boolean addProductClass(String pid, String path, String name, Integer level) {
        String id = CommonUtil.generateUUID16();
        String currPath = "";
        if (!CommonUtil.isNullOrEmpty(pid)) {
            currPath = CommonUtil.isNullOrEmpty(path) ? "" : path + ",";
            currPath += pid;
        }
        int currLevel = level == null ? 0 : level + 1;
        ProductClass productClass = new ProductClass(id, currPath, currLevel, name);
        productClassDao.save(productClass);
        return true;
    }

    @Transactional
    public void deleteProductClass(String id, String path) {
        productClassDao.deleteById(id);
        if (CommonUtil.isNullOrEmpty(path)) {
            productClassDao.deleteByPathLike(id + "%");
        } else {
            productClassDao.deleteByPathLike(path + "," + id + "%");
        }
    }


    public List<ProductClass> getProductClass(String id, String path) {
        if (CommonUtil.isNullOrEmpty(id)) {
            //根节点查询
            return productClassDao.findByLevel(0);
        } else if (CommonUtil.isNullOrEmpty(path)) {
            return productClassDao.findByPath(id);
        } else {
            return productClassDao.findByPath(path + "," + id);
        }
    }

    @Transactional
    public String addProduct(String classId, String name, String price, String specification, String feature, String vehicles) {
        ProductClass productClass = productClassDao.getById(classId);
        if (CommonUtil.isNullOrEmpty(productClass)) {
            logger.info("product class not exist.");
            return "product class not exist.";
        }
        String id = CommonUtil.generateUUID32();
        Product product = new Product(id, productClass.getPath() + "," + productClass.getId(), name, new BigDecimal(price), specification, feature, new Date(), "admin", 0, 0);  //0默认上架
        productDao.save(product);
        if (!CommonUtil.isNullOrEmpty(vehicles)) {
            String[] lists = vehicles.split(",");
            for (int i = 0; i < lists.length; ++i) {
                String result = addVehicleRelationship(id, lists[i]);
                if ("alreadly add".equals(result)) {
                    continue;
                } else if (!"ok".equals(result)) {
                    throw new RuntimeException();
                }
            }
        }
        return "ok";
    }

    public String editProduct(String productId, String classId, String name, String price, String specification, String feature) {
        Product product = productDao.getOne(productId);
        ProductClass productClass = productClassDao.getById(classId);
        if (CommonUtil.isNullOrEmpty(productClass)) {
            logger.info("product class not exist.");
            return "product class not exist.";
        } else {
            product.setClassId(productClass.getPath() + "," + productClass.getId());
        }
        product.setName(name);
        product.setPrice(new BigDecimal(price));
        product.setSpecification(specification);
        product.setFeature(feature);
        productDao.save(product);
        return "ok";
    }

    public List<Product> getProductByClassId(String classId) {
        List<Product> products;
        if (CommonUtil.isNullOrEmpty(classId)) {
            products = productDao.findAll();
        } else {
            ProductClass productClass = productClassDao.getById(classId);
            if (CommonUtil.isNullOrEmpty(productClass)) {
                return null;
            }
            products = productDao.findByClassIdLikeAndDelFlag("%" + classId + "%", 0);
        }
        if (products.isEmpty()) {
            return products;
        }
        products.forEach(product -> transformPhotoUrl(product));
        return products;
    }

    public Page<Product> getProductByClassIdWithPage(String classId, int pageNum, int pageSize) {
        Page<Product> products;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        if (CommonUtil.isNullOrEmpty(classId)) {
            products = productDao.findAll(pageable);
        } else {
            ProductClass productClass = productClassDao.getById(classId);
            if (CommonUtil.isNullOrEmpty(productClass)) {
                return null;
            }
            products = productDao.findByClassIdLikeAndDelFlag("%" + classId + "%", 0, pageable);
        }
        products.forEach(product -> transformPhotoUrl(product));
        return products;
    }

    public void deleteProduct(String productId) {
        Product product = productDao.findByIdAndDelFlag(productId, 0);
        product.setDelFlag(1);
        productDao.save(product);
    }

    public Product getProductById(String id) {
        Product product = productDao.findByIdAndDelFlag(id, 0);
        if (product != null) {
            transformPhotoUrl(product);
        }
        return product;
    }

    private void transformPhotoUrl(Product product) {
        if (CommonUtil.isNullOrEmpty(product.getFeature())) {
            return;
        }
        String[] urls = product.getFeature().split(",");
        StringBuilder sb = new StringBuilder();
        sb.append(QiniuUtil.photoUrlForPublic(urls[0]));
        for (int i = 1; i < urls.length; ++i) {
            sb.append(",").append(QiniuUtil.photoUrlForPublic(urls[i]));
        }
        product.setFeature(sb.toString());
    }

    public String addVehicleRelationship(String productId, String vehicleId) {
        Vehicle vehicle = vehicleDao.findOne(vehicleId);
        Product product = productDao.findByIdAndDelFlag(productId, 0);
        if (CommonUtil.isNullOrEmpty(product)) {
            logger.info("when add vehicleRelationship, product is not exist.");
            return "product is not exist.";
        }
        if (CommonUtil.isNullOrEmpty(vehicle)) {
            logger.info("when add vehicleRelationship, vehicle is not exist.");
            return "vehicle is not exist.";
        }
        VehicleProductRelationship relationship = vehicleProductRelationshipDao.findByVehicleAndProduct(vehicle, product);
        if (!CommonUtil.isNullOrEmpty(relationship)) {
            logger.info("when add vehicleRelationship, alreadly add.");
            return "alreadly add";
        }
        vehicleProductRelationshipDao.save(new VehicleProductRelationship(vehicle, product));
        return "ok";
    }

    @Transactional
    public String addVehicleRelationshipBatch(String productId, List<String> vehicles) {
        for (String vehicle : vehicles) {
            String result = addVehicleRelationship(productId, vehicle);
            if ("alreadly add".equals(result)) {
                continue;
            } else if (!"ok".equals(result)) {
                throw new RuntimeException();
            }
        }
        return "ok";
    }

    @Transactional
    public String deleteVehicleRelationshipBatch(String productId, List<String> vehicles) {
        for (String vehicle : vehicles) {
            String result = deleteVehicleRelation(productId, vehicle);
            if (!"ok".equals(result)) {
                throw new RuntimeException();
            }
        }
        return "ok";
    }

    private String deleteVehicleRelation(String productId, String vehicleId) {
        Vehicle vehicle = vehicleDao.findOne(vehicleId);
        Product product = productDao.findByIdAndDelFlag(productId, 0);
        if (CommonUtil.isNullOrEmpty(product)) {
            logger.info("when delete vehicleRelationship, product is not exist.");
            return "product is not exist.";
        }
        if (CommonUtil.isNullOrEmpty(vehicle)) {
            logger.info("when delete vehicleRelationship, vehicle is not exist.");
            return "vehicle is not exist.";
        }
        VehicleProductRelationship relationship = vehicleProductRelationshipDao.findByVehicleAndProduct(vehicle, product);
        if (CommonUtil.isNullOrEmpty(relationship)) {
            logger.info("when delete vehicleRelationship, alreadly delete.");
            return "alreadly delete.";
        }
        vehicleProductRelationshipDao.delete(relationship);
        return "ok";
    }

    public List<VehicleProductRelationship> getVehicleRelationship(String productId) {
        Product product = productDao.findByIdAndDelFlag(productId, 0);
        if (CommonUtil.isNullOrEmpty(product)) {
            return null;
        }
        List<VehicleProductRelationship> lists = vehicleProductRelationshipDao.findByProduct(product);
        if (lists != null && lists.size() > 0) {
            for (VehicleProductRelationship vp : lists) {
                vp.getVehicle().setName(getFullName(vp.getVehicle().getPath(), vp.getVehicle().getId()));
            }
        }
        return lists;
    }

    private String getFullName(String path, String id) {
        List<String> idList = new ArrayList<String>();
        if (!CommonUtil.isNullOrEmpty(path)) {
            String[] idArr = path.split(",");
            for (String currId : idArr) {
                idList.add(currId);
            }
        }
        idList.add(id);
        List<String> strings = vehicleDao.findVehicleFullName(idList);
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s + " ");
        }
        return sb.toString();
    }

    public List<ProductClass> getProductClassById(String id) {
        String[] strs = id.split(",");
        List<ProductClass> results = new ArrayList<>();
        for (String str : strs) {
            ProductClass pc = productClassDao.findOne(str);
            if (pc != null) {
                results.add(pc);
            }
        }
        return results;
    }

    public void setProductOnSale(String id, String state) {
        Product product = productDao.findByIdAndDelFlag(id, 0);
        product.setOnSale(Integer.parseInt(state));
        productDao.save(product);
    }


    public Page<Product> getProductByClassIdAndVehicleIdWithPage(String classId, String vehicleId, int pageNum, int pageSize) {
        Vehicle vehicle = vehicleDao.findOne(vehicleId);
        if (CommonUtil.isNullOrEmpty(vehicle)) {
            return getProductByClassIdWithPage(classId, pageNum, pageSize);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        Page<Product> products = productDao.findByClassIdAndVehicle(vehicle, "%" + classId + "%", pageable);
        products.forEach(product -> transformPhotoUrl(product));
        return products;
    }

    @Transactional
    public String addProductFroVehicle(String vehicleId, List<String> product) {
        for (String productId : product) {
            String result = addVehicleRelationship(productId, vehicleId);
            if ("alreadly add".equals(result)) {
                continue;
            } else if (!"ok".equals(result)) {
                throw new RuntimeException();
            }
        }
        return "ok";
    }


    @Transactional
    public String deleteProductFroVehicle(String vehicleId, List<String> product) {
        for (String productId : product) {
            String result = deleteVehicleRelation(productId, vehicleId);
            if (!"ok".equals(result)) {
                throw new RuntimeException();
            }
        }
        return "ok";
    }

    public Page<VehicleProductRelationship> getVehicleRelationshipByVehicle(String vehicleId, int pageNum, int pageSize) {
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        if (CommonUtil.isNullOrEmpty(vehicle)) {
            return null;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        Page<VehicleProductRelationship> lists = vehicleProductRelationshipDao.findByVehicle(vehicle, pageable);
        for (VehicleProductRelationship vp : lists) {
            vp.getVehicle().setName(getFullName(vp.getVehicle().getPath(), vp.getVehicle().getId()));
            transformPhotoUrl(vp.getProduct());
        }
        return lists;
    }
}
