package com.example.andrdemocode.ipc;

import static com.example.andrdemocode.ipc.MessengerService.MSG_FROM_C;
import static com.example.andrdemocode.ipc.MessengerService.MSG_FROM_S;
import static com.example.andrdemocode.ipc.MessengerService.MSG_KEY;
import static com.example.andrdemocode.ipc.MessengerService.MSG_KEY_REPLY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.example.andrdemocode.R;
import com.example.andrdemocode.base.XLog;

public class ProcessAActivity extends AppCompatActivity {

    private Messenger messenger;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 通过此对象向服务端发消息
            messenger = new Messenger(service);

            Message message = Message.obtain(null, MSG_FROM_C);
            Bundle bundle = new Bundle();
            bundle.putString(MSG_KEY, "hello service bro");
            message.setData(bundle);

            message.replyTo = getReplyMessenger;

            try {
                messenger.send(message);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Messenger getReplyMessenger = new Messenger(new MessengerHandler(Looper.getMainLooper()));
    private static class MessengerHandler extends Handler {
        public MessengerHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_FROM_S:
                    XLog.i("收到来自service的消息：" + msg.getData().getString(MSG_KEY_REPLY));

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_aactivity);

        bindService(new Intent(this, MessengerService.class), connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}