package com.example.openplatform;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.openplatform.api.OkHttpUtils;
import com.example.openplatform.api.cookie.CookieJarImpl;
import com.example.openplatform.api.cookie.store.PersistentCookieStore;
import com.example.openplatform.api.https.HttpsUtils;
import com.example.openplatform.api.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

public class MyApplication extends Application {

    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        initOkHttp();//配置OkHttpClient
    }

    //获取系统上下文：用于ToastUtil类
    public static Context getAppContext() {
        return mAppContext;
    }

    //配置OkhttpClient
    private void initOkHttp() {
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));//修改成自带的cookie持久化，可以解决程序崩溃时返回到
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);//设置可访问所有的https网站
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))//配置Log,通过设置拦截器实现，框架中提供了一个LoggerInterceptor，当然你可以自行实现一个Interceptor
                .cookieJar(cookieJar)//配置持久化Cookie(包含Session)
                .connectionPool(new ConnectionPool(32, 5, TimeUnit.MINUTES))//自定义连接池最大空闲连接数和等待时间大小，否则默认最大5个空闲连接
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        // TODO Auto-generated method stub
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)//配置Https
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

}
