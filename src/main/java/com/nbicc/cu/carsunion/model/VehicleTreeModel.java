package com.nbicc.cu.carsunion.model;

import java.util.List;

/**
 * Created by bigmao on 2017/9/12.
 */
public class VehicleTreeModel {
    private String id;
    private String name;
    private String logo;
    private String pinyin;
    private String path;
    private int level;
    private List<VehicleTreeModel> child;


    public VehicleTreeModel(String id, String name, String logo, String pinyin, String path, int level) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.pinyin = pinyin;
        this.path = path;
        this.level = level;
    }

    public VehicleTreeModel(String id, String name, String path, int level) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.level = level;
    }

    public VehicleTreeModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public List<VehicleTreeModel> getChild() {
        return child;
    }

    public void setChild(List<VehicleTreeModel> child) {
        this.child = child;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
