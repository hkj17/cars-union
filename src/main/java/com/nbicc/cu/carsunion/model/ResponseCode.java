package com.nbicc.cu.carsunion.model;

import com.nbicc.cu.carsunion.enumtype.ResponseType;

public class ResponseCode {
    private ResponseType responseType;
    private String message;

    public ResponseCode(ResponseType responseType, String message){
        this.responseType = responseType;
        this.message = message;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
