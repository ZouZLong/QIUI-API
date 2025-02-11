package com.example.openplatform.bluetooth.search.response;


import com.example.openplatform.bluetooth.search.SearchResult;

public interface BluetoothSearchResponse {
    void onSearchStarted();

    void onDeviceFounded(SearchResult device);

    void onSearchStopped();

    void onSearchCanceled();
}
