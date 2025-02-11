package com.example.openplatform.bluetooth.connect;


import com.example.openplatform.bluetooth.connect.request.BleRequest;

public interface IBleConnectDispatcher {

    void onRequestCompleted(BleRequest request);
}
