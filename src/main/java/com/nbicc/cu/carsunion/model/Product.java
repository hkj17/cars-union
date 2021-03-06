package com.nbicc.cu.carsunion.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by bigmao on 2017/8/21.
 */
@Entity
public class Product {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "class_id")
    private String classId;

    @Column(name = "name")
    private String name;

    @Column(name = "simple_name")
    private String simpleName;

    @Column(name = "promotion")
    private String promotion;

    @Column(name = "photos")
    private String photos;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "specification")
    private String specification;

    @Column(name = "feature")
    private String feature;

    @Column(name = "created_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @Column(name = "created_by")
    private String admin;

    @Column(name = "on_sale")
    private int onSale;

    @Column(name = "sale_num")
    private int saleNum;

    @Column(name = "group_mark")
    private String groupMark;

    @Column(name = "del_flag")
    private int delFlag;

    @ManyToMany
    @JoinTable(name = "vehicle_product_relationship", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    private Set<Vehicle> vehicles;

    @Transient
    private List<Product> brotherProducts;

    public Product() {
    }

    public Product(String id, String classId, String name, String simpleName, String promotion, String photos, BigDecimal price, String specification, String feature, Date createdTime, String admin, int onSale, int saleNum, String groupMark) {
        this.id = id;
        this.classId = classId;
        this.name = name;
        this.simpleName = simpleName;
        this.promotion = promotion;
        this.photos = photos;
        this.price = price;
        this.specification = specification;
        this.feature = feature;
        this.createdTime = createdTime;
        this.admin = admin;
        this.onSale = onSale;
        this.saleNum = saleNum;
        this.groupMark = groupMark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @JsonIgnore
    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public int getOnSale() {
        return onSale;
    }

    public void setOnSale(int onSale) {
        this.onSale = onSale;
    }

    public int getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(int saleNum) {
        this.saleNum = saleNum;
    }

    @JsonIgnore
    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getGroupMark() {
        return groupMark;
    }

    public void setGroupMark(String groupMark) {
        this.groupMark = groupMark;
    }

    public List<Product> getBrotherProducts() {
        return brotherProducts;
    }

    public void setBrotherProducts(List<Product> brotherProducts) {
        this.brotherProducts = brotherProducts;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }
}
