package com.example.openplatform.activity.equipment

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.openplatform.Config
import com.example.openplatform.R
import com.example.openplatform.activity.BaseActivity
import com.example.openplatform.bean.DecryBluetoothCommandBean
import com.example.openplatform.bean.GetDeviceTokenBean
import com.example.openplatform.bean.MessageEvent
import com.example.openplatform.bean.MqttMsg
import com.example.openplatform.bluetooth.BluetoothClient
import com.example.openplatform.bluetooth.Constants
import com.example.openplatform.bluetooth.connect.listener.BleConnectStatusListener
import com.example.openplatform.bluetooth.connect.response.BleNotifyResponse
import com.example.openplatform.bluetooth.model.BleGattProfile
import com.example.openplatform.bluetooth.search.SearchRequest
import com.example.openplatform.bluetooth.search.SearchResult
import com.example.openplatform.bluetooth.search.response.SearchResponse
import com.example.openplatform.databinding.ActivityGen3Binding
import com.example.openplatform.util.LogUtil
import com.example.openplatform.util.StringUtil
import com.example.openplatform.util.ToastUtil
import com.example.openplatform.vm.MainVm
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Timer
import java.util.TimerTask
import java.util.UUID

/**
 * 三代锁
 */
class Gen3Activity : BaseActivity() {
    private var mac: String? = ""
    private var Api_Token: String? = ""
    private var serialNumber: String? = ""

    private var bluetoothClient: BluetoothClient? = null
    private val delayHandler = Handler() //延迟写入数据
    private var isOpen4G: Boolean = false
    private var timer: Timer? = null //定时器

    private var binding: ActivityGen3Binding? = null
    protected var vm: MainVm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gen3)
        vm = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get<MainVm>(MainVm::class.java)
        binding?.setLifecycleOwner(this) //绑定
        binding?.setOnclick(MyOnclick())
        init()
        initView()
        initData()

        setNavigationBar(0)

        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    fun init() {
        mac = intent.getStringExtra("mac")
        Api_Token = intent.getStringExtra("Api_Token")
        serialNumber = intent.getStringExtra("serialNumber")

        bluetoothClient = BluetoothClient(this)
        bluetoothClient!!.registerConnectStatusListener(mac, mBleConnectStatusListener) //添加监听

        startSearchDevice()
    }

    fun initView() {
    }

    fun deviceToken() { //获取Token
        if (!isOpen4G) {
            vm!!.getDeviceToken(this, Config.httpURL + "/system/api/device/cellMate/bluetooth/buildCellMatePro4GTokenCmd", createLockCmdData(), Api_Token)
        } else {
            vm!!.send4G(this, Config.httpURL + "/system/api/device/cellMate/4g/sendCellMatePro4GTokenCmd", createLockCmdData(), Api_Token)
        }
    }

    fun decryBluetoothCommand(string: String?) { //解密
        val data: MutableMap<String, String?> = HashMap()
        data["lockCommand"] = string
        data["serialNumber"] = serialNumber
        vm!!.decryBluetoothCommand(this, Config.httpURL + "/system/api/device/cellMate/decryBluetoothCommand", data, Api_Token)
    }

    fun keyPodUnlockCmd() { //开锁
        if (!isOpen4G) {
            vm!!.getKeyPodUnlockCmd(this, Config.httpURL + "/system/api/device/cellMate/bluetooth/buildCellMatePro4GUnLockCmd", createLockCmdData(), Api_Token)
        } else {
            vm!!.send4G(this, Config.httpURL + "/system/api/device/cellMate/4g/sendCellMatePro4GUnLockCmd", createLockCmdData(), Api_Token)
        }

    }

    fun keyPodLockCmd() { //关锁
        if (!isOpen4G) {
            vm!!.getKeyPodLockCmd(this, Config.httpURL + "/system/api/device/cellMate/bluetooth/buildCellMatePro4GLockCmd", createLockCmdData(), Api_Token)
        } else {
            vm!!.send4G(this, Config.httpURL + "/system/api/device/cellMate/4g/sendCellMatePro4GLockCmd", createLockCmdData(), Api_Token)
        }
    }

    fun buildCellMateProTimingUnlock() { //定时开锁
        val data: MutableMap<String, Any> = HashMap()
        data["timingDuration"] = binding?.timedUnlocking?.text.toString()
        data["shockVolt"] = binding?.timedUnlockingVoltage?.text.toString()
        data["basicDeviceApiReq"] = createLockCmdData()
        if (!isOpen4G) {
            vm!!.buildCellMateProTimingUnlock(this, Config.httpURL + "/system/api/device/cellMate/bluetooth/buildCellMateProTimingUnlock", data, Api_Token)
        } else {
            vm!!.send4G(this, Config.httpURL + "/system/api/device/cellMate/4g/sendCellMateProTimingUnlock", data, Api_Token)
        }
    }

    fun buildCellMateProClearTimingUnlockCmd() { //清除定时开锁
        if (!isOpen4G) {
            vm!!.buildCellMateProClearTimingUnlockCmd(this, Config.httpURL + "/system/api/device/cellMate/bluetooth/buildCellMateProClearTimingUnlockCmd", createLockCmdData(), Api_Token)
        } else {
            vm!!.send4G(this, Config.httpURL + "/system/api/device/cellMate/4g/sendCellMateProClearTimingUnlockCmd", createLockCmdData(), Api_Token)
        }
    }

    fun buildToyTimingElectricShock() { //定时电击
        val data: MutableMap<String, Any> = HashMap()
        data["shockDuration"] = binding?.shockDuration?.text.toString()
        data["shockModel"] = binding?.shockModel?.text.toString()
        data["shockVolt"] = binding?.shockVolt?.text.toString()
        data["timingDuration"] = binding?.timingDuration?.text.toString()
        data["basicDeviceApiReq"] = createLockCmdData()
        if (!isOpen4G) {
            vm!!.buildToyTimingElectricShock(this, Config.httpURL + "/system/api/device/cellMate/bluetooth/buildToyTimingElectricShock", data, Api_Token)
        } else {
            vm!!.send4G(this, Config.httpURL + "/system/api/device/cellMate/4g/sendToyTimingElectricShock", data, Api_Token)
        }
    }

    fun buildClearAllElectricShockCmd() { //清除定时电击
        if (!isOpen4G) {
            vm!!.buildCellMateProClearTimingUnlockCmd(this, Config.httpURL + "/system/api/device/cellMate/bluetooth/buildClearAllElectricShockCmd", createLockCmdData(), Api_Token)
        } else {
            vm!!.send4G(this, Config.httpURL + "/system/api/device/cellMate/4g/sendClearAllElectricShockCmd", createLockCmdData(), Api_Token)
        }
    }

    fun buildCellMateProShockImmediatelyShockCmd() { //电击一秒
        val data: MutableMap<String, Any> = HashMap()
        data["shockModel"] = binding?.shockModel01?.text.toString().toInt()
        data["shockVolt"] = binding?.shockVolt01?.text.toString().toInt()
        data["basicDeviceApiReq"] = createLockCmdData()
        if (!isOpen4G) {
            vm!!.buildCellMateProShockImmediatelyShockCmd(this, Config.httpURL + "/system/api/device/cellMate/bluetooth/buildCellMateProShockImmediatelyShockCmd", data, Api_Token)
        } else {
            vm!!.send4G(this, Config.httpURL + "/system/api/device/cellMate/4g/sendCellMateProShockImmediatelyShockCmd", data, Api_Token)
        }
    }

    fun buildCellMateProShockContinuedFiveSecond() { //电击五秒
        val data: MutableMap<String, Any> = HashMap()
        data["shockModel"] = binding?.shockModel01?.text.toString().toInt()
        data["shockVolt"] = binding?.shockVolt01?.text.toString().toInt()
        data["basicDeviceApiReq"] = createLockCmdData()
        if (!isOpen4G) {
            vm!!.buildCellMateProShockImmediatelyShockCmd(this, Config.httpURL + "/system/api/device/cellMate/bluetooth/buildCellMateProShockContinuedFiveSecond", data, Api_Token)
        } else {
            vm!!.send4G(this, Config.httpURL + "/system/api/device/cellMate/4g/sendCellMateProShockContinuedFiveSecond", data, Api_Token)
        }
    }

    fun buildCellMateProStopAllShockCmd() { //停止电击
        if (!isOpen4G) {
            vm!!.buildCellMateProShockImmediatelyShockCmd(this, Config.httpURL + "/system/api/device/cellMate/bluetooth/buildCellMateProStopAllShockCmd", createLockCmdData(), Api_Token)
        } else {
            vm!!.send4G(this, Config.httpURL + "/system/api/device/cellMate/4g/sendCellMateProStopAllShockCmd", createLockCmdData(), Api_Token)
        }
    }

    fun buildDisplayDirectionCmd() { //设置显示屏方向
        val data: MutableMap<String, Any> = HashMap()
        data["direction"] = binding?.setDisplayOrientation?.text.toString().toInt()
        data["basicDeviceApiReq"] = createLockCmdData()
        vm!!.send4G(this, Config.httpURL + "/system/api/device/cellMate/bluetooth/buildDisplayDirectionCmd", data, Api_Token)
    }

    fun buildCellMatePro4GWorkModelCmd() { //设置4G工作模式
        val data: MutableMap<String, Any> = HashMap()
        data["workStatus"] = binding?.set4GType01?.text.toString().toInt()
        data["basicDeviceApiReq"] = createLockCmdData()
        if (!isOpen4G) {
            vm!!.buildCellMateProShockImmediatelyShockCmd(this, Config.httpURL + "/system/api/device/cellMate/bluetooth/buildCellMatePro4GWorkModelCmd", data, Api_Token)
        } else {
            vm!!.send4G(this, Config.httpURL + "/system/api/device/cellMate/4g/sendCellMatePro4GWorkModelCmd", data, Api_Token)
        }
    }

    fun buildServerIpAndPortCmd() { //设置MQTT服务
        vm!!.buildCellMateProShockImmediatelyShockCmd(this, Config.httpURL + "/system/api/device/cellMate/bluetooth/buildServerIpAndPortCmd", createLockCmdData(), Api_Token)
    }

    fun sendCellMatePro4GNotifyCmd() { //唤醒设备
        vm!!.send4G(this, Config.httpURL + "/system/api/device/cellMate/4g/sendCellMatePro4GNotifyCmd", createLockCmdData(), Api_Token)
    }

    fun initData() {
        vm!!.mutableLiveData03.observe(this) { data: GetDeviceTokenBean ->  //获取Token
            if (data.code == 200) writeBluetooth(data.data)
        }

        vm!!.mutableLiveData04.observe(this) { data: DecryBluetoothCommandBean ->  //解密
            if (data.code == 200) ToastUtil.showToastCenter("success")
        }

        vm!!.mutableLiveData05.observe(this) { data: GetDeviceTokenBean ->  //开锁
            if (data.code == 200) writeBluetooth(data.data)
        }

        vm!!.mutableLiveData06.observe(this) { data: GetDeviceTokenBean ->  //关锁
            if (data.code == 200) writeBluetooth(data.data)
        }

        vm!!.mutableLiveData08.observe(this) { data: GetDeviceTokenBean ->  //定时开锁
            if (data.code == 200) writeBluetooth(data.data)
        }

        vm!!.mutableLiveData09.observe(this) { data: GetDeviceTokenBean ->  //清除定时开锁 //清除定时电击
            if (data.code == 200) writeBluetooth(data.data)
        }

        vm!!.mutableLiveData10.observe(this) { data: GetDeviceTokenBean ->  //定时电击
            if (data.code == 200) writeBluetooth(data.data)
        }

        //电击一秒 //电击五秒 //停止电击 //设置显示屏方向 //设置4G工作模式 //设置MQTT服务
        vm!!.mutableLiveData11.observe(this) { data: GetDeviceTokenBean ->
            if (data.code == 200) writeBluetooth(data.data)
        }

        vm!!.mutableLiveData12.observe(this) { data: GetDeviceTokenBean -> //发送4G指令
            if (data.code == 200) {

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) fun onMessageEvent(event: MessageEvent) {
        try {
            if (event.data != null && event.data["MqttMsg"] != null) { //MQTT 消息
                val str: String
                val ob: Any = event.data["MqttMsg"]!!
                str = ob.toString()
                LogUtil.loge("str:$str")
                val bean: MqttMsg = Gson().fromJson(str, MqttMsg::class.java)

                LogUtil.loge("bluetoothAddress:" + bean.bluetoothAddress)
                LogUtil.loge("mac:$mac")

                if (bean.bluetoothAddress == mac) { //接受者和设备是自己是才处理相关的命令
                    when (bean.mqttType) {
                        "mqtt_h00001" -> { //唤醒成功
                            stopElectricShock()
                            ToastUtil.showToastCenter(getString(R.string.mylanguage000008))
                            deviceToken()
                        }
                        "mqtt_h00012" -> {

                        }

                    }
                }
            }
        } catch (e: Exception) {
            LogUtil.loge("MqttMsg 错误：$e")
        }
    }

    inner class MyOnclick {
        fun unlock() { //开锁
            keyPodUnlockCmd()
        }

        fun lock() { //关锁
            keyPodLockCmd()
        }

        fun timedUnLocking() { //定时开锁
            buildCellMateProTimingUnlock()
        }

        fun clearTiming() { //清除定时开锁
            buildCellMateProClearTimingUnlockCmd()
        }

        fun myBuildToyTimingElectricShock() { //定时电击
            buildToyTimingElectricShock()
        }

        fun myBuildClearAllElectricShockCmd() { //清除定时电击
            buildClearAllElectricShockCmd()
        }

        fun myBuildCellMateProShockImmediatelyShockCmd(number: Int) {
            if (number == 0) buildCellMateProShockImmediatelyShockCmd() //电击一秒
            if (number == 1) buildCellMateProShockContinuedFiveSecond() //电击五秒
            if (number == 2) buildCellMateProStopAllShockCmd() //停止电击
        }

        fun setDisplayOrientation() { //设置显示屏方向
            buildDisplayDirectionCmd()
        }

        fun set4GType() { //设置4G工作模式
            buildCellMatePro4GWorkModelCmd()
        }

        fun setMQTT() { //设置MQTT服务
            buildServerIpAndPortCmd()
        }

        fun open4G() { //开启4G
            isOpen4G = !isOpen4G
            binding?.open4G?.text = if (isOpen4G) getString(R.string.textlanguage00016) else getString(R.string.textlanguage00015)
            binding?.wakeDevice?.visibility = if (isOpen4G) View.VISIBLE else View.GONE
            if (!isOpen4G) {
                stopElectricShock()
            }
        }

        fun wakeDevice() { //唤醒设备
            staterElectricShock()
        }

        fun closeConn() { //断开连接
            disconnectBluetooth()
            val map: Map<String, Any> = hashMapOf("MqttMsg" to "断开连接").toMap()
            EventBus.getDefault().post(MessageEvent(map))
        }
    }


    fun startSearchDevice() { //连接设备
        val request = SearchRequest.Builder() //搜索设备
            .searchBluetoothLeDevice(3000, 3) // 先扫BLE设备3次，每次3s
            .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
            .searchBluetoothLeDevice(2000) // 再扫BLE设备2s
            .build()
        bluetoothClient!!.search(request, object : SearchResponse {
            override fun onSearchStarted() { //开始连接
            }

            override fun onDeviceFounded(device: SearchResult) { //连接中
                if (device.address == mac) { //将获取的地址 于设备地址进行匹配
                    bluetoothClient!!.stopSearch() //停止搜索设备
                    bluetoothClient!!.connect(device.address) { code: Int, profile: BleGattProfile ->  //连接设备
                        val services = profile.services
                        for (service in services) {
                            val characters = service.characters
                            for (character in characters) { //LogUtil.loge("Uuid:" + character.getUuid() + "  service:" + service.getUUID());
                            }
                        }
                        if (code == Constants.REQUEST_SUCCESS) {
                            bluetoothClient!!.notify(device.address, UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb"), UUID.fromString("000036f6-0000-1000-8000-00805f9b34fb"), object : BleNotifyResponse {
                                //添加监听
                                override fun onNotify(service: UUID, character: UUID, value: ByteArray) {
                                    LogUtil.loge("蓝牙返回:" + StringUtil.byteToHexString(value))
                                    decryBluetoothCommand(StringUtil.byteToHexString(value))
                                }

                                override fun onResponse(code: Int) {
                                    if (code == Constants.REQUEST_SUCCESS) { //监听成功
                                        LogUtil.loge("监听成功")
                                        deviceToken() //获取Token
                                    }
                                }
                            })
                        }
                    }
                }
            }

            override fun onSearchStopped() {
            }

            override fun onSearchCanceled() { //搜索已取消  成功连接到设备后调用这个方法
            }
        })
    }


    //蓝牙状态监听
    private val mBleConnectStatusListener: BleConnectStatusListener = object : BleConnectStatusListener() {
        override fun onConnectStatusChanged(mac: String, status: Int) {
            if (status == Constants.STATUS_CONNECTED) { //先执行这里  再执行下面的REQUEST_SUCCESS状态
                //binding.bluetoothStatusText.setText("开始连接...");
            } else if (status == Constants.STATUS_DISCONNECTED) { //断开连接
                //binding.bluetoothStatusText.setText(getString(R.string.language00088));
            }
        }
    }

    //断开蓝牙的监听
    fun disconnectBluetooth() {
        LogUtil.loge("断开连接")
        if (bluetoothClient != null) {
            bluetoothClient!!.stopSearch() //停止扫描
            bluetoothClient!!.disconnect(mac) //断开连接
            bluetoothClient!!.unregisterConnectStatusListener(mac, mBleConnectStatusListener) //停止监听
        }
    }

    //写入蓝牙命令
    fun writeBluetooth(decryptKey: String) {
        LogUtil.loge("写入蓝牙的命令:$decryptKey")
        delayHandler.postDelayed({
            bluetoothClient!!.write(mac, UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb"), UUID.fromString("000036f5-0000-1000-8000-00805f9b34fb"), StringUtil.hexStr2Bytes(decryptKey)) { code1: Int ->
                if (code1 != Constants.REQUEST_SUCCESS) LogUtil.loge("写入失败：$code1")
                else {
                    LogUtil.loge("success")
                }
            }
        }, 100)
    }

    // 封装参数的方法
    private fun createLockCmdData(): MutableMap<String, Any?> {
        val data: MutableMap<String, Any?> = HashMap()
        data["bluetoothAddress"] = mac
        data["serialNumber"] = serialNumber
        data["typeId"] = 10
        return data
    }

    fun staterElectricShock() { //唤醒设备
        if (timer == null) timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                sendCellMatePro4GNotifyCmd()
            }
        }, 0, 2000)
    }

    fun stopElectricShock() { //停止唤醒
        if (timer != null) {
            timer?.cancel()
            timer = null
        }
    }
}
