package com.example.andrdemocode.dp.chainofrespon;

import android.media.AudioAttributes;

import com.example.andrdemocode.base.XLog;

/**
 * @author dengyan
 * @date 2024/7/6
 * @desc
 */
public class MediaAudioInterceptor extends AudioInterceptor {
    private static final String TAG = "MediaAudioInterceptor";

    @Override
    public boolean intercept(Request request) {
        if (request.getAudioPlaybackConfiguration().getAudioAttributes().getUsage() == AudioAttributes.USAGE_MEDIA) {
            XLog.i(TAG, "拦截到媒体播放请求，处理...");
            return true;
        }
        return super.intercept(request);
    }
}
