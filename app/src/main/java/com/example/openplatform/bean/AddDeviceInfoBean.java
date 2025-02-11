package com.example.openplatform.bean;

import java.io.Serializable;

public class AddDeviceInfoBean  implements Serializable {


    /**
     * code : 200
     * message : Success
     * data : {"id":26,"userId":107,"bluetoothAddress":"17:DA:45:7D:A9:11","serialNumber":"QIUIwwnnk1234567","typeId":6,"environmentType":0}
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
         * id : 26
         * userId : 107
         * bluetoothAddress : 17:DA:45:7D:A9:11
         * serialNumber : QIUIwwnnk1234567
         * typeId : 6
         * environmentType : 0
         */

        private int id;
        private int userId;
        private String bluetoothAddress;
        private String serialNumber;
        private int typeId;
        private int environmentType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getBluetoothAddress() {
            return bluetoothAddress;
        }

        public void setBluetoothAddress(String bluetoothAddress) {
            this.bluetoothAddress = bluetoothAddress;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public int getEnvironmentType() {
            return environmentType;
        }

        public void setEnvironmentType(int environmentType) {
            this.environmentType = environmentType;
        }
    }
}
