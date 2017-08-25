package com.nbicc.cu.carsunion.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "contact")
    private String contact;

    @Column(name = "portrait_path")
    private String portraitPath;

    @Column(name = "credit")
    private int credit;

    @Column(name = "recommend")
    private String recommend;

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

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPortraitPath(){
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath){
        this.portraitPath = portraitPath;
    }

    public int getCredit(){
        return credit;
    }

    public void setCredit(int credit){
        this.credit = credit;
    }

    public String getRecommend(){
        return recommend;
    }

    public void setRecommend(String recommend){
        this.recommend = recommend;
    }
}
