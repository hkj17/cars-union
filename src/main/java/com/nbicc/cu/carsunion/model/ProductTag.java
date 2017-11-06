package com.nbicc.cu.carsunion.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProductTag {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "product_class")
    private String productClass;

    public ProductTag() {
    }

    public ProductTag(String id, String tagName, String productClass) {
        this.id = id;
        this.tagName = tagName;
        this.productClass = productClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return tagName;
    }

    public void setName(String name) {
        this.tagName = name;
    }

    public String getProductClass() {
        return productClass;
    }

    public void setProductClass(String productClass) {
        this.productClass = productClass;
    }
}
