package com.nbicc.cu.carsunion.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Token {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "token")
    private String token;

    @Column(name = "expires_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp expiresAt;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt){
        this.expiresAt = expiresAt;
    }
}
