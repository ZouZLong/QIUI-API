package com.example.openplatform.activity;

import static com.example.openplatform.Config.httpURL;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.openplatform.R;
import com.example.openplatform.activity.equipment.Gen3Activity;
import com.example.openplatform.activity.equipment.KeyPodActivity01;
import com.example.openplatform.databinding.ActivityMainBinding;
import com.example.openplatform.fragment.SearchDeviceDialogFG;
import com.example.openplatform.util.BlueToothUtil;
import com.example.openplatform.util.JurisdictionUtil;
import com.example.openplatform.util.LanguageUtils;
import com.example.openplatform.util.LogUtil;
import com.example.openplatform.util.StatusBarUtils;
import com.example.openplatform.util.ToastUtil;
import com.example.openplatform.vm.MainVm;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private String mac = "";
    private String Api_Token = "";

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
        setNavigationBar(0);
    }


    public void init() {

    }

    public void initView() {

    }

    public void getPlatformApiToken() {//获取平台Token
        Map<String, String> data = new HashMap<>();
        data.put("clientId", "Client_35115347524B4D1CA9A20BF2F660EF49");
        data.put("grantType", "client_credentials");
        vm.getPlatformApiToken(this, httpURL + "/system/api/device/common/getPlatformApiToken", data);
    }

    public void queryDeviceInfo() {//查询设备信息
        Map<String, Object> data = new HashMap<>();
        data.put("bluetoothAddress", mac);
        vm.queryDeviceInfo(this, httpURL + "/system/api/platform/device/queryDeviceInfo", data, Api_Token);
    }

    public void addDeviceInfo() {//平台绑定设备
        Map<String, String> data = new HashMap<>();
        data.put("bluetoothAddress", mac);
        vm.addDeviceInfo(this, httpURL + "/system/api/platform/device/addDeviceInfo", data, Api_Token);
    }


    public void initData() {
        vm.getMutableLiveData01().observe(this, data -> {//获取平台Token
            if (data.getCode() == 200) {
                Api_Token = data.getData().getPlatformApiToken();
                ToastUtil.showToastCenter("API Token：" + Api_Token);
                LogUtil.loge("Api_Token:" + Api_Token);
            } else
                ToastUtil.showToastCenter(data.getMessage());
        });

        vm.getMutableLiveData07().observe(this, data -> {//查询设备信息
            if (data.getCode() == 200) {
                if (data.getData() == null) {
                    //表示平台没有这个设备 先绑定
                    addDeviceInfo();
                } else {
                    Intent intent = null;
                    switch (data.getData().getTypeId()) {
                        case 6://一代钥匙盒
                            intent = new Intent(MainActivity.this, KeyPodActivity01.class);
                            break;
                        case 10://三代锁
                            intent = new Intent(MainActivity.this, Gen3Activity.class);
                            break;
                    }
                    if (intent != null) {
                        intent.putExtra("mac", data.getData().getBluetoothAddress());
                        intent.putExtra("Api_Token", Api_Token);
                        intent.putExtra("serialNumber", data.getData().getSerialNumber());
                        startActivity(intent);
                    }
                }

            } else
                ToastUtil.showToastCenter(data.getMessage());
        });

        vm.getMutableLiveData02().observe(this, data -> {//平台绑定设备
            if (data.getCode() == 200) {
                queryDeviceInfo();
            }
        });

    }


    public class MyOnclick {

        public void getApiToken() {//获取平台Token
            getPlatformApiToken();
        }

        public void conn() {//连接设备
            if (!isOpenBluetooth()) {
                ToastUtil.showToastCenter(getString(R.string.language000381));
                return;
            }
            SearchDeviceDialogFG fragment = new SearchDeviceDialogFG(MainActivity.this);
            fragment.show(getSupportFragmentManager(), fragment.getTag());
        }

        public void changeLanguage() {
            LanguageUtils.changeAppLanguage(MainActivity.this);
        }

        public void test() {
            //Intent intent = new Intent(MainActivity.this, TestActivity.class);
            Intent intent = new Intent(MainActivity.this, Gen3Activity.class);
            startActivity(intent);
        }
    }


    public void getMac(String mac) {
        this.mac = mac;
        queryDeviceInfo();
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
