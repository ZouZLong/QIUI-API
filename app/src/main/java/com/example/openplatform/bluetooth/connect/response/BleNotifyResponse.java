package com.example.openplatform.bluetooth.connect.response;

import java.util.UUID;

/**
 * Created by dingjikerbo on 2016/8/28.
 */
public interface BleNotifyResponse extends BleResponse {

    void onNotify(UUID service, UUID character, byte[] value);
}
