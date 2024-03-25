package com.example.andrdemocode.base;

import android.util.Log;

/**
 * @author dengyan
 * @date 2022/8/8
 * @desc
 */
public class XLog {

    private static final String TAG = "DemoCode";
    private static int aStaticValue;

    public static void i(String format, Object... args) {
        Log.i(TAG, String.format(format, args));
    }

    /**
     * Android为每个应用（或进程）分配独立的虚拟机，多进程下，静态成员和单例失效，不是同一份对象。
     */
    public static void printStaticValue() {
        aStaticValue++;
        XLog.i("printStaticValue " + aStaticValue);
    }

}
