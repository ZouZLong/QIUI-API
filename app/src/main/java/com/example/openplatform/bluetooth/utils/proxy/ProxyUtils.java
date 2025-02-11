package com.example.openplatform.bluetooth.utils.proxy;

import java.lang.reflect.Proxy;

/**
 * Created by dingjikerbo on 2016/8/23.
 */
public class ProxyUtils {

    public static <T> T getProxy(Object object, Class<?>[] intfs, ProxyInterceptor interceptor, boolean weakRef, boolean postUI) {
        return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(),
                intfs, new ProxyInvocationHandler(object, interceptor, weakRef, postUI));
    }

    public static <T> T getProxy(Object object, Class<?> clazz, ProxyInterceptor interceptor, boolean weakRef, boolean postUI) {
        return getProxy(object, new Class<?>[] { clazz }, interceptor, weakRef, postUI);
    }

    public static <T> T getProxy(Object object, Class<?> clazz, ProxyInterceptor interceptor) {
        return getProxy(object, clazz, interceptor, false, false);
    }

    public static <T> T getWeakUIProxy(Object object, Class<?> clazz) {
        return getProxy(object, clazz, null, true, true);
    }

    public static <T> T getUIProxy(Object object) {
        return getUIProxy(object, object.getClass().getInterfaces(), null);
    }

    public static <T> T getUIProxy(Object object, Class<?> clazz) {
        return getUIProxy(object, new Class<?>[] { clazz }, null);
    }

    public static <T> T getUIProxy(Object object, Class<?> clazz, ProxyInterceptor interceptor) {
        return getUIProxy(object, new Class<?>[] { clazz }, interceptor);
    }

    public static <T> T getUIProxy(Object object, Class<?>[] intfs, ProxyInterceptor interceptor) {
        return getProxy(object, intfs, interceptor, false, true);
    }
}
