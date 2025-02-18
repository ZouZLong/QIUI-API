package com.example.openplatform.bean;

import java.util.Map;

public class MessageEvent {
    private String message;
    private String[] strings;
    private Map<String, Object> data;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(Map<String, Object> data){
        this.data = data;
    }

    public MessageEvent(String[] strings){
        this.strings = strings;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setMessage(Map<String, Object> data) {
        this.data = data;
    }

    public String[] getStrings() {
        return strings;
    }

    public void setMessage(String[] strings) {
        this.strings = strings;
    }
}
