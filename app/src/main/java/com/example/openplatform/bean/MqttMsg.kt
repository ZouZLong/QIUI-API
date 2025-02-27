package com.example.openplatform.bean

data class MqttMsg(
    val serialNumber: String,
    val mqttType: String,
    val cmdStatus: String,
    val bluetoothAddress: String,
    val onlineStatus: String,
    val battery: String
)