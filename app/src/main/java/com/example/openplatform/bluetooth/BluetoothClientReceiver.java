package com.example.openplatform.bluetooth;


import com.example.openplatform.bluetooth.connect.listener.BleConnectStatusListener;
import com.example.openplatform.bluetooth.connect.listener.BluetoothStateListener;
import com.example.openplatform.bluetooth.connect.response.BleNotifyResponse;
import com.example.openplatform.bluetooth.receiver.listener.BluetoothBondListener;

import java.util.HashMap;
import java.util.List;


/**
 * Created by liwentian on 2017/1/13.
 */

public class BluetoothClientReceiver {

    private HashMap<String, HashMap<String, List<BleNotifyResponse>>> mNotifyResponses;
    private HashMap<String, List<BleConnectStatusListener>> mConnectStatusListeners;
    private List<BluetoothStateListener> mBluetoothStateListeners;
    private List<BluetoothBondListener> mBluetoothBondListeners;
}
