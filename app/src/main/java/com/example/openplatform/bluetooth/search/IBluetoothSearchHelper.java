package com.example.openplatform.bluetooth.search;


import com.example.openplatform.bluetooth.search.response.BluetoothSearchResponse;

/**
 * Created by dingjikerbo on 2016/8/28.
 */
public interface IBluetoothSearchHelper {

    void startSearch(BluetoothSearchRequest request, BluetoothSearchResponse response);

    void stopSearch();
}
