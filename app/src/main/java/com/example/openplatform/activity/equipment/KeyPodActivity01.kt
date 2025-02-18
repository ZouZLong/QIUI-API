package com.example.openplatform.activity.equipment;

import static com.example.openplatform.Config.httpURL;
import static com.example.openplatform.bluetooth.Constants.REQUEST_SUCCESS;
import static com.example.openplatform.bluetooth.Constants.STATUS_CONNECTED;
import static com.example.openplatform.bluetooth.Constants.STATUS_DISCONNECTED;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.openplatform.R;
import com.example.openplatform.bluetooth.BluetoothClient;
import com.example.openplatform.bluetooth.connect.listener.BleConnectStatusListener;
import com.example.openplatform.bluetooth.connect.response.BleNotifyResponse;
import com.example.openplatform.bluetooth.model.BleGattCharacter;
import com.example.openplatform.bluetooth.model.BleGattProfile;
import com.example.openplatform.bluetooth.model.BleGattService;
import com.example.openplatform.bluetooth.search.SearchRequest;
import com.example.openplatform.bluetooth.search.SearchResult;
import com.example.openplatform.bluetooth.search.response.SearchResponse;
import com.example.openplatform.databinding.ActivityKeyPod01Binding;
import com.example.openplatform.util.BlueToothUtil;
import com.example.openplatform.util.JurisdictionUtil;
import com.example.openplatform.util.LogUtil;
import com.example.openplatform.util.StringUtil;
import com.example.openplatform.util.ToastUtil;
import com.example.openplatform.vm.MainVm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KeypodActivity01 extends AppCompatActivity {

    private String mac = "";
    private String Api_Token = "";
    private String serialNumber = "";

    private BluetoothClient bluetoothClient;
    private Handler delayHandler = new Handler(); //延迟写入数据

    private ActivityKeyPod01Binding binding;
    protected MainVm vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_key_pod_01);
        vm = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(MainVm.class);
        binding.setLifecycleOwner(this); //绑定
        binding.setOnclick(new MyOnclick());
        init();
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectBluetooth();
        if (null != delayHandler) delayHandler = null;
    }

    public void init() {
        mac = getIntent().getStringExtra("mac");
        Api_Token = getIntent().getStringExtra("Api_Token");
        serialNumber = getIntent().getStringExtra("serialNumber");

        bluetoothClient = new BluetoothClient(this);
        bluetoothClient.registerConnectStatusListener(mac, mBleConnectStatusListener); //添加监听

        startSearchDevice();
    }

    public void initView() {

    }

    public void getDeviceToken() {//获取Token
        Map<String, Object> data = new HashMap<>();
        data.put("bluetoothAddress", mac);
        data.put("serialNumber", serialNumber);
        data.put("typeId", 6);
        vm.getDeviceToken(this, httpURL + "/system/api/device/common/getDeviceToken", data, Api_Token);
    }

    public void decryBluetoothCommand(String string) {//解密
        Map<String, String> data = new HashMap<>();
        data.put("lockCommand", string);
        data.put("serialNumber", serialNumber);
        vm.decryBluetoothCommand(this, httpURL + "/system/api/device/keyPod/decryBluetoothCommand", data, Api_Token);
    }

    public void getKeyPodUnlockCmd() {//开锁
        Map<String, Object> data = new HashMap<>();
        data.put("bluetoothAddress", mac);
        data.put("serialNumber", serialNumber);
        data.put("typeId", 6);
        vm.getKeyPodUnlockCmd(this, httpURL + "/system/api/device/keyPod/getKeyPodUnlockCmd", data, Api_Token);
    }

    public void getKeyPodLockCmd() {//关锁
        Map<String, Object> data = new HashMap<>();
        data.put("bluetoothAddress", mac);
        data.put("serialNumber", serialNumber);
        data.put("typeId", 6);
        vm.getKeyPodLockCmd(this, httpURL + "/system/api/device/keyPod/getKeyPodLockCmd", data, Api_Token);
    }


    public void initData() {

        vm.getMutableLiveData03().observe(this, data -> {//获取Token
            if (data.getCode() == 200) {
                writeBluetooth(data.getData());
            }
        });

        vm.getMutableLiveData04().observe(this, data -> {//解密
            if (data.getCode() == 200) {
                ToastUtil.showToastCenter("success");
            }
        });

        vm.getMutableLiveData05().observe(this, data -> {//开锁
            if (data.getCode() == 200) {
                writeBluetooth(data.getData());
            }
        });

        vm.getMutableLiveData06().observe(this, data -> {//关锁
            if (data.getCode() == 200) {
                writeBluetooth(data.getData());
            }
        });

    }


    public class MyOnclick {

        public void unlock() {//开锁
            getKeyPodUnlockCmd();
        }

        public void lock() {//关锁
            getKeyPodLockCmd();
        }

        public void closeConn() {//断开连接
            disconnectBluetooth();
        }

    }


    public void startSearchDevice() {//连接设备
        SearchRequest request = new SearchRequest.Builder() //搜索设备
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();
        bluetoothClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {//开始连接
            }

            @Override
            public void onDeviceFounded(SearchResult device) {//连接中
                if (device.getAddress().equals(mac)) { //将获取的地址 于设备地址进行匹配
                    bluetoothClient.stopSearch();//停止搜索设备
                    bluetoothClient.connect(device.getAddress(), (int code, BleGattProfile profile) -> { //连接设备
                        List<BleGattService> services = profile.getServices();
                        for (BleGattService service : services) {
                            List<BleGattCharacter> characters = service.getCharacters();
                            for (BleGattCharacter character : characters) {
                                //LogUtil.loge("Uuid:" + character.getUuid() + "  service:" + service.getUUID());
                            }
                        }
                        if (code == REQUEST_SUCCESS) {
                            bluetoothClient.notify(device.getAddress(), UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb"),
                                    UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb"), new BleNotifyResponse() {//添加监听
                                        @Override
                                        public void onNotify(UUID service, UUID character, byte[] value) {
                                            LogUtil.loge("蓝牙返回:" + StringUtil.byteToHexString(value));
                                            decryBluetoothCommand(StringUtil.byteToHexString(value));
                                        }

                                        @Override
                                        public void onResponse(int code) {
                                            if (code == REQUEST_SUCCESS) {//监听成功
                                                LogUtil.loge("监听成功");
                                                getDeviceToken();//获取Token
                                            }
                                        }
                                    });
                        }
                    });
                }
            }

            @Override
            public void onSearchStopped() {
            }

            @Override
            public void onSearchCanceled() {//搜索已取消  成功连接到设备后调用这个方法
            }
        });
    }


    //蓝牙状态监听
    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) { //先执行这里  再执行下面的REQUEST_SUCCESS状态
                //binding.bluetoothStatusText.setText("开始连接...");
            } else if (status == STATUS_DISCONNECTED) {//断开连接
                //binding.bluetoothStatusText.setText(getString(R.string.language00088));
            }
        }
    };

    //断开蓝牙的监听
    public void disconnectBluetooth() {
        LogUtil.loge("断开连接");
        if (bluetoothClient != null) {
            bluetoothClient.stopSearch(); //停止扫描
            bluetoothClient.disconnect(mac); //断开连接
            bluetoothClient.unregisterConnectStatusListener(mac, mBleConnectStatusListener); //停止监听
        }
    }

    //写入蓝牙命令
    public void writeBluetooth(String decryptKey) {
        LogUtil.loge("写入蓝牙的命令:" + decryptKey);
        delayHandler.postDelayed(() -> bluetoothClient.write(mac, UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb"),
                UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb"), StringUtil.hexStr2Bytes(decryptKey), code1 -> {
                    if (code1 != REQUEST_SUCCESS) LogUtil.loge("写入失败：" + code1);
                    else {
                        LogUtil.loge("success");
                    }
                }), 100);
    }


}
