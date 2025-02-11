package com.example.openplatform.bluetooth.utils.hook.utils;

/**
 * Created by dingjikerbo on 2016/9/26.
 */
public class Validate {
    public static void isTrue(final boolean expression, final String message, final Object... values) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
}
