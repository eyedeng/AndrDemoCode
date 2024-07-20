package com.example.andrdemocode.cast;

import android.app.ActivityManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioPlaybackConfiguration;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.andrdemocode.utils.ReflectUtil;

import java.util.List;

public class MyAudioPlaybackConfig {
    public static final int PLAYER_STATE_STARTED = 2;
    public static final int PLAYER_STATE_PAUSED = 3;

    private final AudioPlaybackConfiguration mOriginData;
    private final int mUsage;
    private Integer mClientPid;
    private String mPkgName;
    private Integer mState;

    public MyAudioPlaybackConfig(AudioPlaybackConfiguration originData) {
        mOriginData = originData;
        mUsage = originData.getAudioAttributes().getUsage();
    }

    public int getUsage() {
        return mUsage;
    }

    public int getClientPid() {
        if (mClientPid != null) {
            return mClientPid;
        }
        mClientPid = ReflectUtil.callObjectMethod(mOriginData, Integer.class, "getClientPid", null);
        return mClientPid;
    }

    public String getPkgName() {
        if (mPkgName != null) {
            return mPkgName;
        }
        mPkgName = getPackageNameByPid(MainService.getInstance(), getClientPid());
        return mPkgName;
    }

    public static String getPackageNameByPid(Context context, int pid) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> l = mActivityManager.getRunningAppProcesses();
        if (l == null) {
            return "";
        }
        for (ActivityManager.RunningAppProcessInfo info : l) {
            if (info.pid == pid) {
                String[] packages = info.pkgList;
                if (packages.length > 0) {
                    return packages[0];
                }
            }
        }
        return "";
    }

    public int getState() {
        if (mState != null) {
            return mState;
        }
        mState = ReflectUtil.callObjectMethod(mOriginData, Integer.class, "getPlayerState", null);
        return mState;
    }

    public boolean isMediaWorking() {
        return getUsage() == AudioAttributes.USAGE_MEDIA && isWorking() && !TextUtils.isEmpty(getPkgName());
    }

    private boolean isWorking() {
        return isStarted() || isPaused();
    }

    public boolean isStarted() {
        return getState() == PLAYER_STATE_STARTED;
    }

    public boolean isPaused() {
        return getState() == PLAYER_STATE_PAUSED;
    }

    private String stateToString() {
        switch (getState()) {
            case PLAYER_STATE_STARTED:
                return "started";
            case PLAYER_STATE_PAUSED:
                return "paused";
            default:
                return "other";
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "MyAudioPlaybackConfiguration{" +
            "mPkgName=" + getPkgName() +
            ", mState=" + stateToString() +
            ", mUsage=" + getUsage() +
            ", mClientPid=" + getClientPid() +
            '}';
    }
}
