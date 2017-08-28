package com.nbicc.cu.carsunion.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Address {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "address")
    private String address;

    @Column(name = "is_default")
    private Boolean isDefault;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public Boolean getIsDefault(){
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault){
        this.isDefault = isDefault;
    }
}
