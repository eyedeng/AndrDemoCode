package com.example.andrdemocode.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.andrdemocode.base.XLog;

/**
 * @author dengyan
 * @date 2024/3/13
 * @desc
 */
public class MyBindService extends Service {

    private final Binder binder = new DownloadBinder();

    /**
     * 用于Activity和Service通信
     */
    public static class DownloadBinder extends Binder {
        public void startDownload() {
            XLog.i("startDownload");
        }

        public int getProgress() {
            XLog.i("getProgress");
            return 0;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // bindService必须实现
        XLog.i("service onBind");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        XLog.i("service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        XLog.i("service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        XLog.i("service onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        XLog.i("service onUnbind");
        return super.onUnbind(intent);
    }

}
