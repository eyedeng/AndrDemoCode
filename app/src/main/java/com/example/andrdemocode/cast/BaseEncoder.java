package com.example.andrdemocode.cast;

import android.media.MediaCodec;
import android.media.MediaFormat;
import com.example.andrdemocode.base.XLog;

/**
 * @author dengyan
 * @date 2025/2/8
 * @desc 编码器基类，封装了公共的创建、释放操作
 */
public abstract class BaseEncoder {
    protected MediaCodec mediaCodec;
    protected MediaFormat mediaFormat;
    protected MediaMuxerWrapper muxerWrapper;
    protected int trackIndex = -1;
    protected final String TAG = getClass().getSimpleName();

    public BaseEncoder(MediaFormat format, MediaMuxerWrapper muxer) {
        this.mediaFormat = format;
        this.muxerWrapper = muxer;
        try {
            mediaCodec = MediaCodec.createEncoderByType(format.getString(MediaFormat.KEY_MIME));
        } catch (Exception e) {
            XLog.e(TAG, "Failed to create encoder: " + e.getMessage());
        }
    }

    public abstract void prepare();

    // 启动编码器
    public abstract void start();
    // 停止编码器
    public abstract void stop();

    // 释放编码器资源
    public void release() {
        if (mediaCodec != null) {
            mediaCodec.stop();
            mediaCodec.release();
            mediaCodec = null;
        }
    }
}
