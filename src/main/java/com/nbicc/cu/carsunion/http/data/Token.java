package com.nbicc.cu.carsunion.http.data;

public class Token {
    private String token;
    private long expiresAt;

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public long getExpiresAt(){
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt){
        this.expiresAt = expiresAt;
    }
}
