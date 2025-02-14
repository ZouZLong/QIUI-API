package com.example.openplatform.bean;

import java.io.Serializable;

public class QueryDeviceInfoBean implements Serializable {

    /**
     * code : 0
     * data : {"bluetoothAddress":"","createBy":"","createTime":"","environmentType":0,"iccid":"","id":0,"remark":"","serialNumber":"","typeId":0,"updateBy":"","updateTime":"","userId":0}
     * message :
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean implements Serializable{
        /**
         * bluetoothAddress :
         * createBy :
         * createTime :
         * environmentType : 0
         * iccid :
         * id : 0
         * remark :
         * serialNumber :
         * typeId : 0
         * updateBy :
         * updateTime :
         * userId : 0
         */

        private String bluetoothAddress;
        private String createBy;
        private String createTime;
        private int environmentType;
        private String iccid;
        private int id;
        private String remark;
        private String serialNumber;
        private int typeId;
        private String updateBy;
        private String updateTime;
        private int userId;

        public String getBluetoothAddress() {
            return bluetoothAddress;
        }

        public void setBluetoothAddress(String bluetoothAddress) {
            this.bluetoothAddress = bluetoothAddress;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getEnvironmentType() {
            return environmentType;
        }

        public void setEnvironmentType(int environmentType) {
            this.environmentType = environmentType;
        }

        public String getIccid() {
            return iccid;
        }

        public void setIccid(String iccid) {
            this.iccid = iccid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
