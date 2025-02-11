package com.example.openplatform.bean;

import java.io.Serializable;

public class DecryBluetoothCommandBean implements Serializable {

    /**
     * code : 200
     * message : Success
     * data : {"battery":0,"commentType":"01","isUnlocking":false}
     */

    private int code;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * battery : 0
         * commentType : 01
         * isUnlocking : false
         */

        private int battery;
        private String commentType;
        private boolean isUnlocking;

        public int getBattery() {
            return battery;
        }

        public void setBattery(int battery) {
            this.battery = battery;
        }

        public String getCommentType() {
            return commentType;
        }

        public void setCommentType(String commentType) {
            this.commentType = commentType;
        }

        public boolean isIsUnlocking() {
            return isUnlocking;
        }

        public void setIsUnlocking(boolean isUnlocking) {
            this.isUnlocking = isUnlocking;
        }
    }
}
