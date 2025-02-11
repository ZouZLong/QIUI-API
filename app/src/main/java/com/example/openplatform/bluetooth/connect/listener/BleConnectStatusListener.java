package com.example.openplatform.bluetooth.connect.listener;


import com.example.openplatform.bluetooth.receiver.listener.BluetoothClientListener;

/**
 * Created by dingjikerbo on 16/11/26.
 */

public abstract class BleConnectStatusListener extends BluetoothClientListener {

    public abstract void onConnectStatusChanged(String mac, int status);

    @Override
    public void onSyncInvoke(Object... args) {
        String mac = (String) args[0];
        int status = (int) args[1];
        onConnectStatusChanged(mac, status);
    }
}
