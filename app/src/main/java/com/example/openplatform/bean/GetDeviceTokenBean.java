package com.example.openplatform.bean;

import java.io.Serializable;

public class GetDeviceTokenBean implements Serializable {

    /**
     * code : 200
     * message : Success
     * data : 6612C16C7D600088AC8ACA4BA5350281
     */

    private int code;
    private String message;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
