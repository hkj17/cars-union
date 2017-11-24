package com.nbicc.cu.carsunion.model;

public class RegionalInfo {
    private String name;
    private String pinyin;
    private char initial;
    private String longitude;
    private String latitude;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public char getInitial() {
        return initial;
    }

    public void setInitial(char initial) {
        this.initial = initial;
    }

    public String getLongitude(){
        return longitude;
    }

    public void setLongitude(String longitude){
        this.longitude = longitude;
    }

    public String getLatitude(){
        return latitude;
    }

    public void setLatitude(String latitude){
        this.latitude = latitude;
    }
}
