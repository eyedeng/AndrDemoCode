package com.example.andrdemocode.cast;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.IBinder;
import android.view.Display;
import android.view.RoundedCorner;

import androidx.annotation.Nullable;

import com.example.andrdemocode.base.XLog;

/**
 * @author dengyan
 * @date 2024/6/26
 * @desc
 */
public class MainService extends Service {
    private static final String TAG = "MainService";
    private DisplayManager mDisplayManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        XLog.i(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        XLog.i(TAG, "onCreate");
        showRoundedCornerRadius();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        XLog.i(TAG, "onStartCommand action=" + intent.getAction());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XLog.i(TAG, "onDestroy");
    }

    private void showRoundedCornerRadius() {
        mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        int displayId = Display.DEFAULT_DISPLAY;
        Display display = mDisplayManager.getDisplay(displayId);
        RoundedCorner topLeft = display.getRoundedCorner(RoundedCorner.POSITION_TOP_LEFT);
        RoundedCorner topRight = display.getRoundedCorner(RoundedCorner.POSITION_TOP_RIGHT);
        RoundedCorner bottomLeft = display.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_LEFT);
        RoundedCorner bottomRight = display.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_RIGHT);
        printRoundedCorner(topLeft);
        printRoundedCorner(topRight);
        printRoundedCorner(bottomLeft);
        printRoundedCorner(bottomRight);
    }

    private void printRoundedCorner(RoundedCorner roundedCorner) {
        XLog.i(TAG, roundedCorner == null ? "null" : roundedCorner.toString());
    }


}
