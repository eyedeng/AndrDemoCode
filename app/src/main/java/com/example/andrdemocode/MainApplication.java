package com.example.andrdemocode;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;

import com.example.andrdemocode.base.XLog;
import com.example.andrdemocode.cast.MainService;

import java.util.List;

/**
 * @author dengyan
 * @date 2024/3/20
 * @desc
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        showProcess();

        startForegroundService(new Intent(this, MainService.class));
    }

    /**
     * 每启动新进程，会新创建Application
     */
    private void showProcess() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfo = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : processInfo) {
            XLog.i("processName " + process.processName + " " + process.pid);
        }
        XLog.printStaticValue();
    }
}
