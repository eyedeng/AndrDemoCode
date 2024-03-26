package com.example.andrdemocode.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.example.andrdemocode.base.XLog;

/**
 * Messenger进程通信
 */
public class MessengerService extends Service {
    public static final int MSG_FROM_C = 1;
    public static final int MSG_FROM_S = 2;
    public static final String MSG_KEY = "msg";
    public static final String MSG_KEY_REPLY = "reply";

    /**
     * 处理发送给Messenger的Msg
     */
    private static class MessengerHandler extends Handler {
        public MessengerHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_FROM_C:
                    XLog.i("收到来自client的消息：" + msg.getData().getString(MSG_KEY));

                    // 通过replyTo回复客户端
                    Message replyM = Message.obtain(null, MSG_FROM_S);
                    Bundle bundle = new Bundle();
                    bundle.putString(MSG_KEY_REPLY, "消息已收到");
                    replyM.setData(bundle);
                    try {
                        msg.replyTo.send(replyM);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * 将客户端发送的消息传递给MessengerHandler
     */
    private final Messenger messenger = new Messenger(new MessengerHandler(Looper.getMainLooper()));

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

}