package com.nbicc.cu.carsunion.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Advertisement {

    @Id
    @Column
    private Long id;

    @Column
    private Integer location;

    @Column
    private Integer photoType;

    @Column
    private String photoContent;

    @Column
    private String photo;

    @Column
    private Integer isShow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getPhotoType() {
        return photoType;
    }

    public void setPhotoType(Integer photoType) {
        this.photoType = photoType;
    }

    public String getPhotoContent() {
        return photoContent;
    }

    public void setPhotoContent(String photoContent) {
        this.photoContent = photoContent;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }
}
