package com.example.openplatform.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openplatform.R;
import com.example.openplatform.activity.MainActivity;
import com.example.openplatform.adapter.BluetoothNearbyAdapter;
import com.example.openplatform.bluetooth.BluetoothClient;
import com.example.openplatform.bluetooth.search.SearchRequest;
import com.example.openplatform.bluetooth.search.SearchResult;
import com.example.openplatform.bluetooth.search.response.SearchResponse;
import com.example.openplatform.util.LogUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索附近的蓝牙设备
 */
public class SearchDeviceDialogFG extends BottomSheetDialogFragment {

    private final MainActivity activity;//搜索的设备类型

    private final List<String> macList = new ArrayList<>(); //设备MAC
    private final List<String> list = new ArrayList<>(); //展示的数据
    private BluetoothNearbyAdapter adapter;
    private RecyclerView myRecycler;
    private TextView title;

    private View contentView;
    private BottomSheetDialog dialog;

    public SearchDeviceDialogFG(MainActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        // 设置你的布局文件作为对话框的内容视图
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.fg_dialog_search_device, null);
        dialog.setContentView(contentView);
        bingView();
        init();
        initView();
        startSearchDevice();//搜索设备
        return dialog;
    }

    private void init() {
        myRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BluetoothNearbyAdapter(getContext(), list);
        myRecycler.setAdapter(adapter);

        adapter.setOnItemClickListener((view, position) -> {
            activity.getMac(macList.get(position));
            dismiss();
        });
    }

    private void initView() {

    }

    private void bingView() {
        myRecycler = contentView.findViewById(R.id.myRecycler);
        title = contentView.findViewById(R.id.title);
    }

    //搜索设备
    public void startSearchDevice() {
        if (getContext() == null) return;
        BluetoothClient bluetoothClient = new BluetoothClient(getContext());
        SearchRequest request = new SearchRequest.Builder() //搜索设备
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();
        bluetoothClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                title.setText("开始搜索");
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDeviceFounded(SearchResult device) {
                title.setText("附近设备搜索中...");
                if (device.getName() == null || device.getAddress() == null) return;
                if (device.getName().equals("NULL")) return;
                String str = device.getAddress() + " (" + device.getName() + ")";
                if (!macList.contains(device.getAddress())) {// 防止重复添加
                    macList.add(device.getAddress());

                    LogUtil.loge(str);
                    list.add(str);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onSearchStopped() {
                title.setText("搜索完毕");
            }

            @Override
            public void onSearchCanceled() {
                title.setText("搜索已取消");
            }
        });
    }


}
