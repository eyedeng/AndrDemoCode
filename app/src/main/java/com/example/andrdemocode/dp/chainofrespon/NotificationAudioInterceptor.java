package com.example.andrdemocode.dp.chainofrespon;

import android.media.AudioAttributes;

import com.example.andrdemocode.base.XLog;

import java.util.Arrays;
import java.util.List;

/**
 * @author dengyan
 * @date 2024/7/6
 * @desc
 */
public class NotificationAudioInterceptor extends AudioInterceptor {
    private static final String TAG = "NotificationAudioInterceptor";
    private static final List<Integer> NOTIFICATION_USAGES = Arrays.asList(
            AudioAttributes.USAGE_NOTIFICATION,
            AudioAttributes.USAGE_NOTIFICATION_RINGTONE,
            AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST,
            AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT,
            AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED,
            AudioAttributes.USAGE_NOTIFICATION_EVENT
    );

    @Override
    public boolean intercept(Request request) {
        if (isNotificationUsage(request.getAudioPlaybackConfiguration().getAudioAttributes().getUsage())) {
            XLog.i(TAG, "拦截到通知播放请求，处理...");
            return true;
        }
        return super.intercept(request);
    }

    private boolean isNotificationUsage(int usage) {
        return NOTIFICATION_USAGES.contains(usage);
    }

}
