package com.example.openplatform.bean;

import java.io.Serializable;

public class GetPlatformApiTokenBean implements Serializable {


    /**
     * code : 200
     * message : Success
     * data : {"platformApiToken":"eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjljNzg1OTNiLWQ2MjItNDQ3MC05YWVhLWJmZTNjMzNhMDNmMyJ9.e_1vlmt37gbuYYYksPdXhQ0QyOHPeKv-IBW1TPVdUemRryv625iFTPmCwXc8mmFjmju8H4wCjQfl5Acn2JYQFA","expiresTime":43193}
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

    public static class DataBean implements Serializable {
        /**
         * platformApiToken : eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjljNzg1OTNiLWQ2MjItNDQ3MC05YWVhLWJmZTNjMzNhMDNmMyJ9.e_1vlmt37gbuYYYksPdXhQ0QyOHPeKv-IBW1TPVdUemRryv625iFTPmCwXc8mmFjmju8H4wCjQfl5Acn2JYQFA
         * expiresTime : 43193
         */

        private String platformApiToken;
        private int expiresTime;

        public String getPlatformApiToken() {
            return platformApiToken;
        }

        public void setPlatformApiToken(String platformApiToken) {
            this.platformApiToken = platformApiToken;
        }

        public int getExpiresTime() {
            return expiresTime;
        }

        public void setExpiresTime(int expiresTime) {
            this.expiresTime = expiresTime;
        }
    }
}
