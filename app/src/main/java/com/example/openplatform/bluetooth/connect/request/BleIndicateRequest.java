package com.example.openplatform.bluetooth.connect.request;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;

import com.example.openplatform.bluetooth.Code;
import com.example.openplatform.bluetooth.Constants;
import com.example.openplatform.bluetooth.connect.listener.WriteDescriptorListener;
import com.example.openplatform.bluetooth.connect.response.BleGeneralResponse;

import java.util.UUID;


/**
 * Created by dingjikerbo on 2016/11/1.
 */

public class BleIndicateRequest extends BleRequest implements WriteDescriptorListener {

    private final UUID mServiceUUID;
    private final UUID mCharacterUUID;

    public BleIndicateRequest(UUID service, UUID character, BleGeneralResponse response) {
        super(response);
        mServiceUUID = service;
        mCharacterUUID = character;
    }

    @Override
    public void processRequest() {
        switch (getCurrentStatus()) {
            case Constants.STATUS_DEVICE_DISCONNECTED:
                onRequestCompleted(Code.REQUEST_FAILED);
                break;

            case Constants.STATUS_DEVICE_CONNECTED:
                openIndicate();
                break;

            case Constants.STATUS_DEVICE_SERVICE_READY:
                openIndicate();
                break;

            default:
                onRequestCompleted(Code.REQUEST_FAILED);
                break;
        }
    }

    private void openIndicate() {
        if (!setCharacteristicIndication(mServiceUUID, mCharacterUUID, true)) {
            onRequestCompleted(Code.REQUEST_FAILED);
        } else {
            startRequestTiming();
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGattDescriptor descriptor, int status) {
        stopRequestTiming();

        if (status == BluetoothGatt.GATT_SUCCESS) {
            onRequestCompleted(Code.REQUEST_SUCCESS);
        } else {
            onRequestCompleted(Code.REQUEST_FAILED);
        }
    }
}
