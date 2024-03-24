package com.example.andrdemocode.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.andrdemocode.IMyAidlInterface;
import com.example.andrdemocode.R;

public class ServiceActivity extends AppCompatActivity {

    private MyBindService.DownloadBinder binder;

    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 这样其它组件就可以控制Service做事了
            binder = (MyBindService.DownloadBinder) service;
            binder.startDownload();
            binder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        findViewById(R.id.start_s).setOnClickListener(v -> {
            // 启动Service后和this组件没大关系了，应用进程内，如不主动调用stopSelf()/stopService()，将一直运行
            startService(new Intent(this, HelloService.class));
        });
        findViewById(R.id.stop_s).setOnClickListener(v -> {
            stopService(new Intent(this, HelloService.class));
        });

        findViewById(R.id.bind_s).setOnClickListener(v -> {
            // 获取this组件和Service的持久连接，this和service连接断开service就停止运行（没有被其它组件bind或start时）
            bindService(new Intent(this, MyBindService.class), conn, BIND_AUTO_CREATE);
        });
        findViewById(R.id.unbind_s).setOnClickListener(v -> {
            unbindService(conn);
        });

        aidl();
    }

    private IMyAidlInterface aidlInterface;

    private final ServiceConnection colorConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 定义了AIDLColorService的为服务端，S、C公用一套aidl文件IMyAidlInterface
     * 客户端可以调用服务（跨进程通信）
     */
    private void aidl() {
        findViewById(R.id.client_app).setOnClickListener(v -> {
            Intent intent = new Intent("AIDLColorService");
            intent.setPackage("com.example.andrdemocode");
            bindService(intent, colorConn, BIND_AUTO_CREATE);
        });
        findViewById(R.id.get_color).setOnClickListener(v -> {
            try {
                v.setBackgroundColor(aidlInterface.getColor());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }
}