package com.example.andrdemocode.base;

import android.util.Log;

/**
 * @author dengyan
 * @date 2022/8/8
 * @desc
 */
public class XLog {

    private static final String TAG = "DemoCode";

    public static void i(String format, Object... args) {
        Log.i(TAG, String.format(format, args));
    }
}
