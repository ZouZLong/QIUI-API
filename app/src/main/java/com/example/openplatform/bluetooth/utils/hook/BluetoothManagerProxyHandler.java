package com.example.openplatform.bluetooth.utils.hook;

import android.os.IBinder;
import android.os.IInterface;

import com.example.openplatform.bluetooth.utils.BluetoothLog;
import com.example.openplatform.bluetooth.utils.hook.utils.HookUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * Created by dingjikerbo on 16/9/2.
 */
public class BluetoothManagerProxyHandler implements InvocationHandler {

    private final Object iBluetoothManager;

    private final Class<?> bluetoothGattClaz;
    private final Object bluetoothGatt;

    BluetoothManagerProxyHandler(Object iBluetoothManager) {
        this.iBluetoothManager = iBluetoothManager;

        this.bluetoothGattClaz = HookUtils.getClass("android.bluetooth.IBluetoothGatt");
        Class<?> stub = HookUtils.getClass("android.bluetooth.IBluetoothManager");
        Method method = HookUtils.getMethod(stub, "getBluetoothGatt");
        this.bluetoothGatt = HookUtils.invoke(method, iBluetoothManager);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        BluetoothLog.v(String.format("IBluetoothManager method: %s", method.getName()));

        if ("getBluetoothGatt".equals(method.getName())) {
            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),
                    new Class<?>[] {IBinder.class, IInterface.class, bluetoothGattClaz},
                    new BluetoothGattProxyHandler(bluetoothGatt));
        }
        return method.invoke(iBluetoothManager, args);
    }
}
