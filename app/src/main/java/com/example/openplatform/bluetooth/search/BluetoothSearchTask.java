package com.example.openplatform.bluetooth.search;



import static com.example.openplatform.bluetooth.Constants.SEARCH_TYPE_BLE;
import static com.example.openplatform.bluetooth.Constants.SEARCH_TYPE_CLASSIC;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.openplatform.bluetooth.search.response.BluetoothSearchResponse;


public class BluetoothSearchTask implements Handler.Callback {

	private static final int MSG_SEARCH_TIMEOUT = 0x22;

	private int searchType;

	private int searchDuration;

	private BluetoothSearcher mBluetoothSearcher;

	private final Handler mHandler;

	public BluetoothSearchTask(SearchTask task) {
		setSearchType(task.getSearchType());
		setSearchDuration(task.getSearchDuration());
		mHandler = new Handler(Looper.myLooper(), this);
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public void setSearchDuration(int searchDuration) {
		this.searchDuration = searchDuration;
	}

	public boolean isBluetoothLeSearch() {
		return searchType == SEARCH_TYPE_BLE;
	}

	public boolean isBluetoothClassicSearch() {
		return searchType == SEARCH_TYPE_CLASSIC;
	}

	private BluetoothSearcher getBluetoothSearcher() {
		if (mBluetoothSearcher == null) {
			mBluetoothSearcher = BluetoothSearcher.newInstance(searchType);
		}
		return mBluetoothSearcher;
	}

	public void start(BluetoothSearchResponse response) {
		getBluetoothSearcher().startScanBluetooth(response);
		mHandler.sendEmptyMessageDelayed(MSG_SEARCH_TIMEOUT, searchDuration);
	}

	public void cancel() {
		mHandler.removeCallbacksAndMessages(null);
		getBluetoothSearcher().cancelScanBluetooth();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String type = "";
		if (isBluetoothLeSearch()) {
			type = "Ble";
		} else if (isBluetoothClassicSearch()) {
			type = "classic";
		} else {
			type = "unknown";
		}
		
		if (searchDuration >= 1000) {
			return String.format("%s search (%ds)", type, searchDuration / 1000);
		} else {
			return String.format("%s search (%.1fs)", type, 1.0 * searchDuration / 1000);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
			case MSG_SEARCH_TIMEOUT:
				getBluetoothSearcher().stopScanBluetooth();
				break;
		}
		return true;
	}
}
