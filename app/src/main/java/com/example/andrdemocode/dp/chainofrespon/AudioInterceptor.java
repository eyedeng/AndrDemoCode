package com.example.andrdemocode.dp.chainofrespon;

import android.media.AudioPlaybackConfiguration;

/**
 * @author dengyan
 * @date 2024/7/6
 * @desc 责任链模式
 */
public abstract class AudioInterceptor {
    private AudioInterceptor mNextInterceptor;

    public AudioInterceptor chain(AudioInterceptor interceptor) {
        if (mNextInterceptor == null) {
            mNextInterceptor = interceptor;
        } else {
            mNextInterceptor.chain(interceptor);
        }
        return this;
    }

    public boolean intercept(Request request) {
        if (mNextInterceptor == null) {
            return false;
        }
        return mNextInterceptor.intercept(request);
    }

    public static class Request {
        private final AudioPlaybackConfiguration mAudioPlaybackConfiguration;
        public Request(AudioPlaybackConfiguration audioPlaybackConfiguration) {
            mAudioPlaybackConfiguration = audioPlaybackConfiguration;
        }

        public AudioPlaybackConfiguration getAudioPlaybackConfiguration() {
            return mAudioPlaybackConfiguration;
        }
    }

}
