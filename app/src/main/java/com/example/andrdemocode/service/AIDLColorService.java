package com.example.andrdemocode.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.andrdemocode.IMyAidlInterface;
import com.example.andrdemocode.base.XLog;

import java.util.Random;

public class AIDLColorService extends Service {

    private final IMyAidlInterface.Stub binder = new IMyAidlInterface.Stub() {
        @Override
        public int getColor() throws RemoteException {
            Random random = new Random();
            int color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            XLog.i("getColor " + color);
            return color;
        }
    };

    public AIDLColorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        XLog.i("AIDLColorService onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        XLog.i("AIDLColorService onBind");
        return binder;
    }
}