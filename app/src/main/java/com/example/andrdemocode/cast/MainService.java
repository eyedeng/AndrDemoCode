package com.example.andrdemocode.cast;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.media.AudioManager;
import android.media.AudioPlaybackConfiguration;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Display;
import android.view.RoundedCorner;

import androidx.annotation.Nullable;

import com.example.andrdemocode.base.XLog;
import com.example.andrdemocode.dp.chainofrespon.AudioInterceptor;
import com.example.andrdemocode.dp.chainofrespon.MediaAudioInterceptor;
import com.example.andrdemocode.dp.chainofrespon.NotificationAudioInterceptor;
import com.example.andrdemocode.utils.ServiceUtil;

import java.util.List;

/**
 * @author dengyan
 * @date 2024/6/26
 * @desc
 */
public class MainService extends Service {
    private static final String TAG = "MainService";
    private static Context sInstance;
    private DisplayManager mDisplayManager;
    private AudioManager mAudioManager;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public static Context getInstance() {
         return sInstance;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        XLog.i(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        XLog.i(TAG, "onCreate");
        useForegroundService();
        init();
        test();
    }

    private void test() {
//        showRoundedCornerRadius();
        audio();

    }

    @Deprecated
    private void mediaSession() {
        MediaSessionManager mediaSessionManager = (MediaSessionManager) getSystemService(MEDIA_SESSION_SERVICE);
        // 获取不到MEDIA_CONTENT_CONTROL权限
        List<MediaController> mediaControllers = mediaSessionManager.getActiveSessions(null);
        mediaControllers.forEach(controller -> {
            XLog.i(TAG, "has mediaSession pkg: " + controller.getPackageName());
        });

    }

    private AudioManager.AudioPlaybackCallback mAudioPlaybackCallback;
    private AudioInterceptor mAudioInterceptor;

    public void audio() {
        mAudioInterceptor = (new NotificationAudioInterceptor())
            .chain(new MediaAudioInterceptor());
        mAudioPlaybackCallback = new AudioManager.AudioPlaybackCallback() {
            @Override
            public void onPlaybackConfigChanged(List<AudioPlaybackConfiguration> configs) {
                XLog.i(TAG, "==========>");
                configs.forEach(config -> {
                    XLog.i(TAG, "onPlaybackConfigChanged config=" + config);
                    mAudioInterceptor.intercept(new AudioInterceptor.Request(config));
                });
                configs.stream()
                    .map(MyAudioPlaybackConfig::new)
                    .forEach(config -> {
                        XLog.i(TAG, "my config=" + config);
                    });
                super.onPlaybackConfigChanged(configs);
            }
        };
        mAudioManager.registerAudioPlaybackCallback(mAudioPlaybackCallback, null);
    }

    public void showRoundedCornerRadius() {
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

    private void init() {
        mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    private void useForegroundService() {
        ServiceUtil.createNotificationChannel(this);
        startForeground(1, ServiceUtil.getForegroundNotification(this, "主服务", "test code is running..."));
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
        if(mAudioPlaybackCallback != null) {
            mAudioManager.unregisterAudioPlaybackCallback(mAudioPlaybackCallback);
            mAudioPlaybackCallback = null;
        }
        mAudioInterceptor = null;
    }

}
