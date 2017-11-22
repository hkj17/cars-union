package com.nbicc.cu.carsunion.util;

import com.nbicc.cu.carsunion.http.MHGetVTypeHttpRequest;

public class TestMH {
    public static void main(String[] args){
        MHGetVTypeHttpRequest request = new MHGetVTypeHttpRequest();
        System.out.println(request.getResponseGET());
    }
}
