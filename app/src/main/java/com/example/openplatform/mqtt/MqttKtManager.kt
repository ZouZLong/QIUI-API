package com.example.openplatform.mqtt

import android.annotation.SuppressLint
import android.content.Context
import com.example.openplatform.Config.MqttPassWord
import com.example.openplatform.Config.MqttUrl
import com.example.openplatform.Config.MqttUserName
import com.example.openplatform.bean.MessageEvent
import com.example.openplatform.util.LogUtil
import info.mqtt.android.service.MqttAndroidClient
import info.mqtt.android.service.QoS
import org.eclipse.paho.client.mqttv3.*
import org.greenrobot.eventbus.EventBus

class MqttKtManager {

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var mqttAndroidClient: MqttAndroidClient? = null // Make it nullable
        private lateinit var mqttTopics: List<String> // 使用List来存储多个主题
        private var isInitialized = false // Add a flag to track initialization

        fun init(context: Context, clientId: String, mqttTopics: List<String>) {
            try {
                this.mqttTopics = mqttTopics
                mqttAndroidClient = MqttAndroidClient(context, MqttUrl, clientId) // Initialize here

                mqttAndroidClient?.setCallback(object : MqttCallbackExtended {
                    override fun connectComplete(reconnect: Boolean, serverURI: String) {
                        if (reconnect) {
                            LogUtil.loge("MqttKtManager", "连接成功 ：$serverURI")
                            subscribeToTopics()
                        }
                    }

                    override fun connectionLost(cause: Throwable) {
                        LogUtil.loge("MqttKtManager", "The Connection was lost.   $cause")
                        subscribeToTopics()
                    }

                    @Throws(Exception::class)
                    override fun messageArrived(topic: String, message: MqttMessage) {
                        try {
                            LogUtil.loge("MqttKtManager", "MQTT 收到消息：$message")
                            val map: Map<String, Any> =
                                hashMapOf("MqttMsg" to message.toString()).toMap()
                            EventBus.getDefault().post(MessageEvent(map))
                        } catch (e: Exception) {
                            LogUtil.loge("MqttKtManager", "messageArrived 错误：$e")
                        }
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken) {}
                })

                val mqttConnectOptions = MqttConnectOptions()
                mqttConnectOptions.isAutomaticReconnect = true
                mqttConnectOptions.isCleanSession = true
                mqttConnectOptions.userName = MqttUserName
                mqttConnectOptions.password = MqttPassWord.toCharArray()
                mqttConnectOptions.connectionTimeout = 100
                mqttConnectOptions.keepAliveInterval = 20
                mqttAndroidClient?.connect(mqttConnectOptions, null, object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken) {
                        subscribeToTopics()
                        LogUtil.loge("MqttKtManager", "连接成功")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                        LogUtil.loge("MqttKtManager", "连接失败：$exception")
                    }
                })
                isInitialized = true // Set the flag to true after successful initialization
            } catch (e: Exception) {
                LogUtil.loge("MqttKtManager", "init 错误：$e")
            }
        }

        private fun subscribeToTopics() { // Make it private
            if (!isInitialized || mqttAndroidClient == null) {
                LogUtil.loge(
                    "MqttKtManager",
                    "subscribeToTopics called before initialization or mqttAndroidClient is null"
                )
                return
            }
            try {
                val disconnectedBufferOptions = DisconnectedBufferOptions()
                disconnectedBufferOptions.isBufferEnabled = true
                disconnectedBufferOptions.bufferSize = 100
                disconnectedBufferOptions.isPersistBuffer = false
                disconnectedBufferOptions.isDeleteOldestMessages = false
                mqttAndroidClient?.setBufferOpts(disconnectedBufferOptions)

                mqttTopics.forEach { topic ->
                    mqttAndroidClient?.subscribe(
                        topic,
                        QoS.AtMostOnce.value,
                        null,
                        object : IMqttActionListener {
                            override fun onSuccess(asyncActionToken: IMqttToken) {
                                LogUtil.loge("MqttKtManager", "订阅成功：$topic")
                            }

                            override fun onFailure(
                                asyncActionToken: IMqttToken,
                                exception: Throwable
                            ) {
                                LogUtil.loge("MqttKtManager", "订阅失败：$topic，异常：$exception")
                            }
                        })
                }
            } catch (e: Exception) {
                LogUtil.loge("MqttKtManager", "subscribeToTopics 错误：$e")
            }
        }


        fun publishMessage(mqttTopic: String, msg: String) {
            if (!isInitialized || mqttAndroidClient == null) {
                LogUtil.loge(
                    "MqttKtManager",
                    "publishMessage called before initialization or mqttAndroidClient is null"
                )
                return
            }
            try {
                LogUtil.loge("MqttKtManager", "主题:$mqttTopic --> 命令:$msg")
                if (mqttAndroidClient?.isConnected == true) {
                    mqttAndroidClient?.publish(mqttTopic, msg.toByteArray(), 0, false)
                } else {
                    LogUtil.loge("MqttKtManager", "发送失败")
                }
            } catch (e: Exception) {
                LogUtil.loge("MqttKtManager", "publishMessage 错误：$e")
            }
        }

        fun stopMqtt() {
            if (!isInitialized || mqttAndroidClient == null) {
                LogUtil.loge(
                    "MqttKtManager",
                    "stopMqtt called before initialization or mqttAndroidClient is null"
                )
                return
            }
            try {
                if (mqttAndroidClient?.isConnected == true) {
                    mqttAndroidClient?.close()
                    mqttAndroidClient?.disconnect()
                    mqttAndroidClient?.unregisterResources()
                    LogUtil.loge("MqttKtManager", "停止成功")
                }
            } catch (e: Exception) {
                LogUtil.loge("MqttKtManager", "停止MQTT 错误：$e")
            }
        }


        fun subscribeToTopic(mqttTopic: String) {
            if (!isInitialized || mqttAndroidClient == null) {
                LogUtil.loge(
                    "MqttKtManager",
                    "subscribeToTopic called before initialization or mqttAndroidClient is null"
                )
                return
            }
            try {
                val disconnectedBufferOptions = DisconnectedBufferOptions()
                disconnectedBufferOptions.isBufferEnabled = true
                disconnectedBufferOptions.bufferSize = 100
                disconnectedBufferOptions.isPersistBuffer = false
                disconnectedBufferOptions.isDeleteOldestMessages = false
                mqttAndroidClient?.setBufferOpts(disconnectedBufferOptions)

                mqttAndroidClient?.subscribe(
                    mqttTopic,
                    QoS.AtMostOnce.value,
                    null,
                    object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken) {
                            LogUtil.loge("MqttKtManager", "onSuccess")
                        }

                        override fun onFailure(
                            asyncActionToken: IMqttToken?,
                            exception: Throwable?
                        ) {
                            LogUtil.loge("MqttKtManager", "onFailure $exception")
                        }
                    })
            } catch (e: Exception) {
                LogUtil.loge("MqttKtManager", "subscribeToTopic 错误：$e")
            }
        }

        fun unsubscribeFromTopic(mqttTopic: String) {
            if (!isInitialized || mqttAndroidClient == null) {
                LogUtil.loge(
                    "MqttKtManager",
                    "unsubscribeFromTopic called before initialization or mqttAndroidClient is null"
                )
                return
            }
            try {
                mqttAndroidClient?.unsubscribe(
                    mqttTopic,
                    null,
                    object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken) {
                            LogUtil.loge(
                                "MqttKtManager",
                                "Unsubscribed successfully from $mqttTopic"
                            )
                        }

                        override fun onFailure(
                            asyncActionToken: IMqttToken?,
                            exception: Throwable?
                        ) {
                            LogUtil.loge(
                                "MqttKtManager",
                                "Failed to unsubscribe from $mqttTopic: $exception"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                LogUtil.loge("MqttKtManager", "unsubscribeFromTopic 错误：$e")
            }
        }
    }
}