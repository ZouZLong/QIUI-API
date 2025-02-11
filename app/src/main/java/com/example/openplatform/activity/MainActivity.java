package com.example.openplatform.activity;

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
import com.example.openplatform.databinding.ActivityMainBinding;
import com.example.openplatform.fragment.SearchDeviceDialogFG;
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

public class MainActivity extends AppCompatActivity {

    private String httpURL = "http://192.168.31.163:8115";

    private String Api_Token = "";

    private BluetoothClient bluetoothClient;
    private String mac = "";
    private Handler delayHandler = new Handler(); //延迟写入数据

    protected ActivityMainBinding binding;
    protected MainVm vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
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
        if (null != delayHandler) delayHandler = null;//悬浮框销毁
    }

    public void init() {
        bluetoothClient = new BluetoothClient(this);
    }

    public void initView() {

    }

    public void getPlatformApiToken() {//获取平台Token
        Map<String, String> data = new HashMap<>();
        data.put("clientId", "Client_35115347524B4D1CA9A20BF2F660EF49");
        data.put("grantType", "client_credentials");
        vm.getPlatformApiToken(this, httpURL + "/system/api/device/common/getPlatformApiToken", data);
    }

    public void addDeviceInfo() {//平台绑定设备
        Map<String, String> data = new HashMap<>();
        data.put("bluetoothAddress", mac);
        vm.addDeviceInfo(this, httpURL + "/system/api/platform/device/addDeviceInfo", data, Api_Token);
    }

    public void getDeviceToken() {//获取Token
        Map<String, Object> data = new HashMap<>();
        data.put("bluetoothAddress", mac);
        data.put("serialNumber", "QIUIwwnnk1234567");
        data.put("typeId", 6);
        vm.getDeviceToken(this, httpURL + "/system/api/device/common/getDeviceToken", data, Api_Token);
    }


    public void decryBluetoothCommand(String string) {//解密
        Map<String, String> data = new HashMap<>();
        data.put("lockCommand", string);
        data.put("serialNumber", "QIUIwwnnk1234567");
        vm.decryBluetoothCommand(this, httpURL + "/system/api/device/keyPod/decryBluetoothCommand", data, Api_Token);
    }

    public void getKeyPodUnlockCmd() {//开锁
        Map<String, Object> data = new HashMap<>();
        data.put("bluetoothAddress", mac);
        data.put("serialNumber", "QIUIwwnnk1234567");
        data.put("typeId", 6);
        vm.getKeyPodUnlockCmd(this, httpURL + "/system/api/device/keyPod/getKeyPodUnlockCmd", data, Api_Token);
    }

    public void getKeyPodLockCmd() {//关锁
        Map<String, Object> data = new HashMap<>();
        data.put("bluetoothAddress", mac);
        data.put("serialNumber", "QIUIwwnnk1234567");
        data.put("typeId", 6);
        vm.getKeyPodLockCmd(this, httpURL + "/system/api/device/keyPod/getKeyPodLockCmd", data, Api_Token);
    }

    public void initData() {
        vm.getMutableLiveData01().observe(this, data -> {//获取平台Token
            if (data.getCode() == 200) {
                Api_Token = data.getData().getPlatformApiToken();
                LogUtil.loge("Api_Token:" + Api_Token);
            }
        });

        vm.getMutableLiveData02().observe(this, data -> {//平台绑定设备
            if (data.getCode() == 200) {
                ToastUtil.showToastCenter("success");
            }
        });

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

        public void getApiToken() {//获取平台Token
            getPlatformApiToken();
        }

        public void conn() {//连接设备
            if (!isOpenBluetooth()) {
                ToastUtil.showToastCenter(getString(R.string.language00101));
                return;
            }
            SearchDeviceDialogFG fragment = new SearchDeviceDialogFG(MainActivity.this);
            fragment.show(getSupportFragmentManager(), fragment.getTag());
        }

        public void bind() {//绑定设备
            addDeviceInfo();
        }

        public void getToken() {//获取Token
            getDeviceToken();
        }

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


    public void getMac(String mac) {
        this.mac = mac;
        bluetoothClient.registerConnectStatusListener(mac, mBleConnectStatusListener); //添加监听
        startSearchDevice();
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
                                            LogUtil.loge("蓝牙返回:"+StringUtil.byteToHexString(value));
                                            decryBluetoothCommand(StringUtil.byteToHexString(value));
                                        }

                                        @Override
                                        public void onResponse(int code) {
                                            if (code == REQUEST_SUCCESS) {//监听成功
                                                LogUtil.loge("监听成功");
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
        if (bluetoothClient != null) {
            bluetoothClient.stopSearch(); //停止扫描
            bluetoothClient.disconnect(mac); //断开连接
            bluetoothClient.unregisterConnectStatusListener(mac, mBleConnectStatusListener); //停止监听
        }
    }

    //写入蓝牙命令
    public void writeBluetooth(String decryptKey) {
        delayHandler.postDelayed(() -> bluetoothClient.write(mac, UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb"),
                UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb"), StringUtil.hexStr2Bytes(decryptKey), code1 -> {
                    if (code1 != REQUEST_SUCCESS) LogUtil.loge("写入失败：" + code1);
                    else {
                        LogUtil.loge("success");
                    }
                }), 100);
    }


    public boolean isOpenBluetooth() {
        final boolean[] aBoolean = {false};
        // 请求蓝牙权限
        JurisdictionUtil.requestBluetoothPermissions(this, new JurisdictionUtil.PermissionCallback() {
            @Override
            public void onGranted() {
                // 所有权限已授权
                Log.d("MainActivity", "所有权限已授权");
                JurisdictionUtil.showToastCenter(MainActivity.this, "所有权限已授权");

                if (BlueToothUtil.getInstance().orBlue(MainActivity.this)) { //打开了蓝牙
                    try {
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                        if (gps || network) {
                            aBoolean[0] = true;
                        } else {
                            openGPS();
                        }
                    } catch (Exception e) {
                        aBoolean[0] = true;
                    }
                }
            }

            @Override
            public void onDenied() {
                // 有权限被拒绝
                Log.d("MainActivity", "有权限被拒绝");
                JurisdictionUtil.showToastCenter(MainActivity.this, "有权限被拒绝");
            }
        });
        return aBoolean[0];
    }

    //强制帮用户打开GPS
    public final void openGPS() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(settingsIntent);
    }


}
