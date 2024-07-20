package com.example.andrdemocode.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.andrdemocode.base.XLog;
import com.example.andrdemocode.utils.ServiceUtil;

/**
 * @author dengyan
 * @date 2024/3/7
 * @desc
 */
public class HelloService extends Service {
    private static final int WHAT_UI_UPDATE = 1;
    private ServiceHandler serviceHandler;
    private Handler uiHandler;

    private class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            XLog.i("handleMessage");
            try {
                Thread.sleep(msg.arg2 * 1000);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
            XLog.i("handleMessage done");
            uiHandler.obtainMessage(WHAT_UI_UPDATE, msg.arg2).sendToTarget();
//            stopSelf(msg.arg1);
            serviceHandler.obtainMessage(1, msg.arg1, msg.arg2 + 1).sendToTarget();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        XLog.i("service onCreate");
        // 服务默认运行在主线程，需开子线程执行耗时操作
        HandlerThread ht = new HandlerThread("myName", Process.THREAD_PRIORITY_BACKGROUND);
        ht.start();
        serviceHandler = new ServiceHandler(ht.getLooper());
        uiHandler = new Handler(Looper.getMainLooper(), msg -> {
            switch (msg.what) {
                case WHAT_UI_UPDATE:
                    Toast.makeText(this, "handleMessage " + (int) msg.obj, Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        });

        useForegroundService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        XLog.i("service onStartCommand");
        Message message = serviceHandler.obtainMessage();
        message.arg1 = startId;
        int processingTime = 5;
        message.arg2 = processingTime;
        serviceHandler.sendMessage(message);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        XLog.i("service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        XLog.i("service onDestroy");
        // 停止后台线程
        Looper looper = serviceHandler.getLooper();
        looper.quit();
    }

    private void useForegroundService() {
        // 前台服务有个通知显示给用户，系统不轻易回收服务
        // 使用startForegroundService(intent)启动服务
        startForeground(1, createNotification());
    }

    private Notification createNotification() {
        ServiceUtil.createNotificationChannel(this);
        return ServiceUtil.getForegroundNotification(this, "服务",
            "your music is playing", ServiceActivity.class);
    }
}
