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
import com.example.openplatform.databinding.ActivityAnalPlug2Binding
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
 * 二代肛塞 有加密
 */
class AnalPlug2Activity : BaseActivity() {
    private var mac: String? = ""
    private var Api_Token: String? = ""
    private var serialNumber: String? = ""

    private var bluetoothClient: BluetoothClient? = null
    private val delayHandler = Handler() //延迟写入数据

    private lateinit var binding: ActivityAnalPlug2Binding
    private lateinit var vm: MainVm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_anal_plug2)
        vm = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[MainVm::class.java]
        binding.setLifecycleOwner(this) //绑定
        binding.setOnclick(MyOnclick())
        setNavigationBar(0)


        init()
        initView()
        initData()
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
        vm.getDeviceToken(this, Config.httpURL + "/system/api/device/pearFlowerThree/getToyToken", createLockCmdData(), Api_Token)
    }

    fun decryBluetoothCommand(string: String?) { //解密
        val data: MutableMap<String, String?> = HashMap()
        data["lockCommand"] = string
        data["serialNumber"] = serialNumber
        vm.decryBluetoothCommand(this, Config.httpURL + "/system/api/device/pearFlowerThree/decryBluetoothCommand", data, Api_Token)
    }

    fun keyPodUnlockCmd() { //开锁
        vm.getKeyPodUnlockCmd(this, Config.httpURL + "/system/api/device/pearFlowerThree/generatePearFlowerThreeToyUnlock", createLockCmdData(), Api_Token)
    }

    fun keyPodLockCmd() { //关锁
        vm.getKeyPodLockCmd(this, Config.httpURL + "/system/api/device/pearFlowerThree/generatePearFlowerThreeToyLock", createLockCmdData(), Api_Token)
    }


    fun generatePearFlowerThreeToyVibration() { //开始震动
        val data: MutableMap<String, Any> = HashMap()
        data["vibrationIntensity"] = binding.editText01.text.toString().toInt()
        data["vibrationModel"] = binding.editText02.text.toString().toInt()
        data["basicDeviceApiReq"] = createLockCmdData()
        vm.getShakeMetalLockImmediatelyShakeCmd(this, Config.httpURL + "/system/api/device/pearFlowerThree/generatePearFlowerThreeToyVibration", data, Api_Token)
    }

    fun generatePearFlowerThreeToyCancelVibration() { //停止震动
        vm.stopAllShakeAndElectricCmd(this, Config.httpURL + "/system/api/device/pearFlowerThree/generatePearFlowerThreeToyCancelVibration", createLockCmdData(), Api_Token)
    }

    fun generatePearFlowerThreeToyTimingVibration() { //定时震动
        val data: MutableMap<String, Any> = HashMap()
        data["vibrationIntensity"] = binding.editText01.text.toString().toInt()
        data["vibrationModel"] = binding.editText02.text.toString().toInt()
        data["vibrationDuration"] = binding.editText03.text.toString().toInt() //震动时长
        data["timingTime"] = "10"//倒计时
        data["basicDeviceApiReq"] = createLockCmdData()
        vm.time(this, Config.httpURL + "/system/api/device/pearFlowerThree/generatePearFlowerThreeToyTimingVibration", data, Api_Token)
    }

    fun generatePearFlowerThreeToyCancelTimingVibration() { //删除定时震动
        vm.time(this, Config.httpURL + "/system/api/device/pearFlowerThree/generatePearFlowerThreeToyCancelTimingVibration", createLockCmdData(), Api_Token)
    }

    fun generatePearFlowerThreeToyElectric() { //开始电击
        val data: MutableMap<String, Any> = HashMap()
        data["electricModel"] = binding.editText01.text.toString().toInt()
        data["electricIntensity"] = binding.editText02.text.toString().toInt()
        data["electricDuration"] = "99"
        data["basicDeviceApiReq"] = createLockCmdData()
        vm.generatePearFlowerThreeToyElectric(this, Config.httpURL + "/system/api/device/pearFlowerThree/generatePearFlowerThreeToyElectric", data, Api_Token)
    }

    fun generatePearFlowerThreeToyCancelElectric() { //停止电击
        vm.generatePearFlowerThreeToyCancelElectric(this, Config.httpURL + "/system/api/device/pearFlowerThree/generatePearFlowerThreeToyCancelElectric", createLockCmdData(), Api_Token)
    }

    fun generatePearFlowerThreeToyTimingElectric() { //定时电击
        val data: MutableMap<String, Any> = HashMap()
        data["electricIntensity"] = binding.editText01.text.toString().toInt()
        data["electricModel"] = binding.editText02.text.toString().toInt()
        data["electricDuration"] = binding.editText03.text.toString().toInt() //震动时长
        data["timingTime"] = "10"//倒计时
        data["basicDeviceApiReq"] = createLockCmdData()
        vm.time(this, Config.httpURL + "/system/api/device/pearFlowerThree/generatePearFlowerThreeToyTimingElectric", data, Api_Token)
    }

    fun generatePearFlowerThreeToyCancelTimingElectric() { //删除定时电击
        vm.time(this, Config.httpURL + "/system/api/device/pearFlowerThree/generatePearFlowerThreeToyCancelTimingElectric", createLockCmdData(), Api_Token)
    }


    fun generatePearFlowerThreeToyIsOpenOfflineFlag(openOfflineFlag: String) { //离线模式
        val data: MutableMap<String, Any> = HashMap()
        data["openOfflineFlag"] = openOfflineFlag
        data["basicDeviceApiReq"] = createLockCmdData()
        vm.time(this, Config.httpURL + "/system/api/device/pearFlowerThree/generatePearFlowerThreeToyIsOpenOfflineFlag", data, Api_Token)
    }

    fun initData() {
        vm.mutableLiveData03.observe(this) { data: GetDeviceTokenBean ->  //获取Token
            if (data.code == 200) writeBluetooth(data.data)
        }

        vm.mutableLiveData04.observe(this) { data: DecryBluetoothCommandBean ->  //解密
            if (data.code == 200) ToastUtil.showToastCenter("success")
        }

        vm.mutableLiveData05.observe(this) { data: GetDeviceTokenBean ->  //开锁
            if (data.code == 200) writeBluetooth(data.data)
        }

        vm.mutableLiveData06.observe(this) { data: GetDeviceTokenBean ->  //关锁
            if (data.code == 200) writeBluetooth(data.data)
        }

        vm.mutableLiveData13.observe(this) { data: GetDeviceTokenBean ->  //开始震动
            if (data.code == 200) writeBluetooth(data.data)
        }

        vm.mutableLiveData14.observe(this) { data: GetDeviceTokenBean ->  //停止震动
            if (data.code == 200) writeBluetooth(data.data)
        }

        vm.mutableLiveData15.observe(this) { data: GetDeviceTokenBean ->  //立即电击
            if (data.code == 200) writeBluetooth(data.data)
        }

        vm.mutableLiveData16.observe(this) { data: GetDeviceTokenBean ->  //停止电击
            if (data.code == 200) writeBluetooth(data.data)
        }

        vm.mutableLiveData17.observe(this) { data: GetDeviceTokenBean ->  //定时
            if (data.code == 200) writeBluetooth(data.data)
        }

    }


    inner class MyOnclick {
        fun unlock() { //开锁
            keyPodUnlockCmd()
        }

        fun lock() { //关锁
            keyPodLockCmd()
        }

        fun generatePearFlowerThreeToyVibration() { //开始震动
            this@AnalPlug2Activity.generatePearFlowerThreeToyVibration()
        }

        fun generatePearFlowerThreeToyCancelVibration() { //停止震动
            this@AnalPlug2Activity.generatePearFlowerThreeToyCancelVibration()
        }

        fun generatePearFlowerThreeToyTimingVibration() { //定时震动
            this@AnalPlug2Activity.generatePearFlowerThreeToyTimingVibration()
        }

        fun generatePearFlowerThreeToyCancelTimingVibration() {//取消定时震动
            this@AnalPlug2Activity.generatePearFlowerThreeToyCancelTimingVibration()
        }

        fun generatePearFlowerThreeToyElectric() { //开始电击
            this@AnalPlug2Activity.generatePearFlowerThreeToyElectric()
        }

        fun generatePearFlowerThreeToyCancelElectric() { //停止电击
            this@AnalPlug2Activity.generatePearFlowerThreeToyCancelElectric()
        }

        fun generatePearFlowerThreeToyTimingElectric() { //定时电击
            this@AnalPlug2Activity.generatePearFlowerThreeToyTimingElectric()
        }

        fun generatePearFlowerThreeToyCancelTimingElectric() { //取消定时电击
            this@AnalPlug2Activity.generatePearFlowerThreeToyCancelTimingElectric()
        }


        fun enableOffline() { //开启离线
            generatePearFlowerThreeToyIsOpenOfflineFlag("1")
        }

        fun offOffline() { //关闭离线
            generatePearFlowerThreeToyIsOpenOfflineFlag("2")
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
                            bluetoothClient!!.notify(device.address, UUID.fromString("0000ac3a-0000-1000-8000-00805f9b34fb"), UUID.fromString("0000ac3c-0000-1000-8000-00805f9b34fb"), object : BleNotifyResponse {
                                //添加监听
                                override fun onNotify(service: UUID, character: UUID,
                                                      value: ByteArray) {
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
            bluetoothClient!!.write(mac, UUID.fromString("0000ac3a-0000-1000-8000-00805f9b34fb"), UUID.fromString("0000ac3b-0000-1000-8000-00805f9b34fb"), StringUtil.hexStr2Bytes(decryptKey)) { code1: Int ->
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
        data["typeId"] = 18
        return data
    }


}
