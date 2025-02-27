package com.example.openplatform;


public class Config {

    /**
     * 本地测试环境
     */
//    public static final String httpURL = "http://192.168.31.163:8115";//1冠 3称 4本地测试 5内网穿透


    /**
     * 正式环境
     */
    public static final String httpURL = "https://openapi.qiuitoy.com";





    public static final String MqttUserName = "mqtt_usr_7253ae60";//MQTT账号
    public static final String MqttPassWord = "mqtt_pwd_65abd826";//MQTT密码
    public static final String MqttUrl = "tcp://openmq.qiuitoy.com:1883";//MQTT地址

}
