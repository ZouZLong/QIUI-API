package com.example.openplatform.vm;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.openplatform.api.OkHttpUtils;
import com.example.openplatform.api.callback.StringCallback;
import com.example.openplatform.bean.AddDeviceInfoBean;
import com.example.openplatform.bean.DecryBluetoothCommandBean;
import com.example.openplatform.bean.GetDeviceTokenBean;
import com.example.openplatform.bean.GetPlatformApiTokenBean;
import com.example.openplatform.util.EncryptUtil;
import com.example.openplatform.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

public class MainVm extends ViewModel {


    private MutableLiveData<GetPlatformApiTokenBean> mutableLiveData01;
    private GetPlatformApiTokenBean value01;

    public MutableLiveData<GetPlatformApiTokenBean> getMutableLiveData01() {
        if (mutableLiveData01 == null) {
            mutableLiveData01 = new MutableLiveData<>();
            if (value01 != null) mutableLiveData01.setValue(value01);
        }
        return mutableLiveData01;
    }

    public void getPlatformApiToken(Context context, String url, Map<String, String> data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            OkHttpUtils.postString().url(url).addHeader("Environment", "TEST")
                    .content(jsonObject.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .tag(context).build().execute(new StringCallback() {
                        @Override
                        public void onBefore(Request request, int id) {
                            LogUtil.loge("获取平台Token:onBefore");
                        }

                        @Override
                        public void onAfter(int id) {
                            LogUtil.loge("获取平台Token:onAfter");
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtil.loge("获取平台Token:" + e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                LogUtil.loge("获取平台Token:" + response);
                                value01 = new Gson().fromJson(response, GetPlatformApiTokenBean.class);
                                mutableLiveData01.setValue(value01);
                            } catch (Exception e) {
                                LogUtil.loge("获取平台Token 错误：" + e);
                            }
                        }
                    });
        } catch (Exception e) {
            LogUtil.loge("网络异常信息：" + e.toString());
        }
    }


    private MutableLiveData<AddDeviceInfoBean> mutableLiveData02;
    private AddDeviceInfoBean value02;

    public MutableLiveData<AddDeviceInfoBean> getMutableLiveData02() {
        if (mutableLiveData02 == null) {
            mutableLiveData02 = new MutableLiveData<>();
            if (value01 != null) mutableLiveData02.setValue(value02);
        }
        return mutableLiveData02;
    }

    public void addDeviceInfo(Context context, String url, Map<String, String> data, String APi_Token) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String string = EncryptUtil.encrypt(jsonObject.toString());
            LogUtil.loge("string:" + string);
            OkHttpUtils.postString().url(url).addHeader("Environment", "TEST").addHeader("Authorization", APi_Token)
                    .content(string).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .tag(context).build().execute(new StringCallback() {
                        @Override
                        public void onBefore(Request request, int id) {
                            LogUtil.loge("平台绑定设备:onBefore");
                        }

                        @Override
                        public void onAfter(int id) {
                            LogUtil.loge("平台绑定设备:onAfter");
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtil.loge("平台绑定设备:" + e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                LogUtil.loge("平台绑定设备:" + response);
                                response = EncryptUtil.decrypt(response);
                                LogUtil.loge("平台绑定设备:" + response);
                                value02 = new Gson().fromJson(response, AddDeviceInfoBean.class);
                                mutableLiveData02.setValue(value02);
                            } catch (Exception e) {
                                LogUtil.loge("平台绑定设备 错误：" + e);
                            }
                        }
                    });
        } catch (Exception e) {
            LogUtil.loge("网络异常信息：" + e.toString());
        }
    }


    private MutableLiveData<GetDeviceTokenBean> mutableLiveData03;
    private GetDeviceTokenBean value03;

    public MutableLiveData<GetDeviceTokenBean> getMutableLiveData03() {
        if (mutableLiveData03 == null) {
            mutableLiveData03 = new MutableLiveData<>();
            if (value01 != null) mutableLiveData03.setValue(value03);
        }
        return mutableLiveData03;
    }

    public void getDeviceToken(Context context, String url, Map<String, Object> data, String APi_Token) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String string = EncryptUtil.encrypt(jsonObject.toString());
            OkHttpUtils.postString().url(url).addHeader("Environment", "TEST").addHeader("Authorization", APi_Token)
                    .content(string).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .tag(context).build().execute(new StringCallback() {
                        @Override
                        public void onBefore(Request request, int id) {
                            LogUtil.loge("获取Token:onBefore");
                        }

                        @Override
                        public void onAfter(int id) {
                            LogUtil.loge("获取Token:onAfter");
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtil.loge("获取Token:" + e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                LogUtil.loge("获取Token:" + response);
                                response = EncryptUtil.decrypt(response);
                                LogUtil.loge("获取Token:" + response);
                                value03 = new Gson().fromJson(response, GetDeviceTokenBean.class);
                                mutableLiveData03.setValue(value03);
                            } catch (Exception e) {
                                LogUtil.loge("获取Token 错误：" + e);
                            }
                        }
                    });
        } catch (Exception e) {
            LogUtil.loge("网络异常信息：" + e.toString());
        }
    }


    private MutableLiveData<DecryBluetoothCommandBean> mutableLiveData04;
    private DecryBluetoothCommandBean value04;

    public MutableLiveData<DecryBluetoothCommandBean> getMutableLiveData04() {
        if (mutableLiveData04 == null) {
            mutableLiveData04 = new MutableLiveData<>();
            if (value01 != null) mutableLiveData04.setValue(value04);
        }
        return mutableLiveData04;
    }

    public void decryBluetoothCommand(Context context, String url, Map<String, String> data, String APi_Token) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String string = EncryptUtil.encrypt(jsonObject.toString());
            OkHttpUtils.postString().url(url).addHeader("Environment", "TEST").addHeader("Authorization", APi_Token)
                    .content(string).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .tag(context).build().execute(new StringCallback() {
                        @Override
                        public void onBefore(Request request, int id) {
                            LogUtil.loge("解密:onBefore");
                        }

                        @Override
                        public void onAfter(int id) {
                            LogUtil.loge("解密:onAfter");
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtil.loge("解密:" + e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                LogUtil.loge("解密:" + response);
                                response = EncryptUtil.decrypt(response);
                                LogUtil.loge("解密:" + response);
                                value04 = new Gson().fromJson(response, DecryBluetoothCommandBean.class);
                                mutableLiveData04.setValue(value04);
                            } catch (Exception e) {
                                LogUtil.loge("解密 错误：" + e);
                            }
                        }
                    });
        } catch (Exception e) {
            LogUtil.loge("网络异常信息：" + e.toString());
        }
    }


    private MutableLiveData<GetDeviceTokenBean> mutableLiveData05;
    private GetDeviceTokenBean value05;

    public MutableLiveData<GetDeviceTokenBean> getMutableLiveData05() {
        if (mutableLiveData05 == null) {
            mutableLiveData05 = new MutableLiveData<>();
            if (value01 != null) mutableLiveData05.setValue(value05);
        }
        return mutableLiveData05;
    }

    public void getKeyPodUnlockCmd(Context context, String url, Map<String, Object> data, String APi_Token) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String string = EncryptUtil.encrypt(jsonObject.toString());
            OkHttpUtils.postString().url(url).addHeader("Environment", "TEST").addHeader("Authorization", APi_Token)
                    .content(string).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .tag(context).build().execute(new StringCallback() {
                        @Override
                        public void onBefore(Request request, int id) {
                            LogUtil.loge("开锁:onBefore");
                        }

                        @Override
                        public void onAfter(int id) {
                            LogUtil.loge("开锁:onAfter");
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtil.loge("开锁:" + e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                LogUtil.loge("开锁:" + response);
                                response = EncryptUtil.decrypt(response);
                                LogUtil.loge("开锁:" + response);
                                value05 = new Gson().fromJson(response, GetDeviceTokenBean.class);
                                mutableLiveData05.setValue(value05);
                            } catch (Exception e) {
                                LogUtil.loge("开锁 错误：" + e);
                            }
                        }
                    });
        } catch (Exception e) {
            LogUtil.loge("网络异常信息：" + e.toString());
        }
    }


    private MutableLiveData<GetDeviceTokenBean> mutableLiveData06;
    private GetDeviceTokenBean value06;

    public MutableLiveData<GetDeviceTokenBean> getMutableLiveData06() {
        if (mutableLiveData06 == null) {
            mutableLiveData06 = new MutableLiveData<>();
            if (value01 != null) mutableLiveData06.setValue(value05);
        }
        return mutableLiveData06;
    }

    public void getKeyPodLockCmd(Context context, String url, Map<String, Object> data, String APi_Token) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String string = EncryptUtil.encrypt(jsonObject.toString());
            OkHttpUtils.postString().url(url).addHeader("Environment", "TEST").addHeader("Authorization", APi_Token)
                    .content(string).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .tag(context).build().execute(new StringCallback() {
                        @Override
                        public void onBefore(Request request, int id) {
                            LogUtil.loge("关锁:onBefore");
                        }

                        @Override
                        public void onAfter(int id) {
                            LogUtil.loge("关锁:onAfter");
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtil.loge("关锁:" + e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                LogUtil.loge("关锁:" + response);
                                response = EncryptUtil.decrypt(response);
                                LogUtil.loge("关锁:" + response);
                                value06 = new Gson().fromJson(response, GetDeviceTokenBean.class);
                                mutableLiveData06.setValue(value06);
                            } catch (Exception e) {
                                LogUtil.loge("关锁 错误：" + e);
                            }
                        }
                    });
        } catch (Exception e) {
            LogUtil.loge("网络异常信息：" + e.toString());
        }
    }


}
