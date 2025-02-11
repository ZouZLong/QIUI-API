package com.example.openplatform.bluetooth.connect.request;


import com.example.openplatform.bluetooth.connect.IBleConnectDispatcher;

/**
 * Created by dingjikerbo on 16/8/25.
 */
public interface IBleRequest {

    void process(IBleConnectDispatcher dispatcher);

    void cancel();
}
