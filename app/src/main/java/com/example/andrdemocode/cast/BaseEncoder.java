package com.example.andrdemocode.cast;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.andrdemocode.base.XLog;

/**
 * @author dengyan
 * @date 2025/2/8
 * @desc 编码器基类，封装了公共的创建、释放操作
 */
public abstract class BaseEncoder {
    protected final String TAG = getClass().getSimpleName();
    protected MediaCodec mediaCodec;
    protected MediaFormat mediaFormat;
    protected MediaMuxerWrapper muxerWrapper;
    protected int trackIndex = -1;
    protected volatile boolean stopped = false;
    private HandlerThread handlerThread;
    private Handler handler;

    public BaseEncoder(MediaFormat format, MediaMuxerWrapper muxer) {
        this.mediaFormat = format;
        this.muxerWrapper = muxer;
        try {
            mediaCodec = MediaCodec.createEncoderByType(format.getString(MediaFormat.KEY_MIME));
        } catch (Exception e) {
            XLog.e(TAG, "Failed to create encoder: " + e.getMessage());
        }
    }

    public void prepare() {
        XLog.i(TAG, "prepare");
        if (handler == null) {
            handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            handler = new EncoderHandler(handlerThread.getLooper());
        }
        handler.sendEmptyMessage(EncoderHandler.PREPARE);
    }

    public void start() {
        XLog.i(TAG, "start");
        if (handler != null) {
            handler.sendEmptyMessage(EncoderHandler.START);
        }
    }

    public void stop() {
        XLog.i(TAG, "stop");
        if (handler != null) {
            handler.sendEmptyMessage(EncoderHandler.STOP);
        }
    }

    // 释放编码器资源
    public void release() {
        XLog.i(TAG, "release");
        if (handler != null) {
            handler.sendEmptyMessage(EncoderHandler.RELEASE);
        }
    }

    public abstract void doPrepare();
    public abstract void doStart();
    public abstract void doStop();

    public void doRelease() {
        XLog.i(TAG, "doRelease");
        if (mediaCodec != null) {
            mediaCodec.stop();
            mediaCodec.release();
            mediaCodec = null;
        }
        if (handlerThread != null) {
            handlerThread.quitSafely();
            handlerThread = null;
            handler = null;
        }
    }

    private class EncoderHandler extends Handler {
        public static final int PREPARE = 1;
        private static final int START = 2;
        private static final int STOP = 3;
        private static final int RELEASE = 4;

        public EncoderHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            XLog.i(TAG, "handleMessage: " + msg.what);
            switch (msg.what) {
                case PREPARE:
                    doPrepare();
                    break;
                case START:
                    doStart();
                    break;
                case STOP:
                    doStop();
                    break;
                case RELEASE:
                    doRelease();
                    break;
                default:
                    break;
            }
        }
    }

}
