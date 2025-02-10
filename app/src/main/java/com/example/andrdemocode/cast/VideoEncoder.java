package com.example.andrdemocode.cast;

import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.view.Surface;
import com.example.andrdemocode.base.XLog;
import java.nio.ByteBuffer;

/**
 * 视频编码器，使用 Surface 作为数据输入
 */
public class VideoEncoder extends BaseEncoder {

    private Surface inputSurface;
    private DisplayConfig displayConfig;
    private final MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private Surface tempSurface;

    public VideoEncoder(DisplayConfig displayConfig, MediaMuxerWrapper muxer, MediaProjection mediaProjection,
                        Surface tempSurface) {
        super(createVideoFormat(displayConfig), muxer);
        this.displayConfig = displayConfig;
        this.mediaProjection = mediaProjection;
        this.tempSurface = tempSurface;
    }

    // 根据 DisplayConfig 创建视频编码格式
    private static MediaFormat createVideoFormat(DisplayConfig config) {
        MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC,
                config.getWidth(), config.getHeight());
        format.setInteger(MediaFormat.KEY_BIT_RATE, config.getBitRate());
        format.setInteger(MediaFormat.KEY_FRAME_RATE, config.getFrameRate());
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
        return format;
    }

    // 返回编码器输入的 Surface，用于绑定 VirtualDisplay
    public Surface getInputSurface() {
        return inputSurface;
    }

    @Override
    public void prepare() {
        initVirtualDisplay();
        initEncoder();
    }

    private void initVirtualDisplay() {
        XLog.i(TAG, "Setting up a VirtualDisplay: " +
                displayConfig.getWidth() + "x" + displayConfig.getHeight() +
                " (" + displayConfig.getDensity() + ")");
        int flags = 0;
        if (displayConfig.isMirror()) {
            flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR;
        } else {
            flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY;
        }
        virtualDisplay = mediaProjection.createVirtualDisplay(
                "Demo-ScreenRecorder",
                displayConfig.getWidth(),
                displayConfig.getHeight(),
                displayConfig.getDensity(),
                flags,
                tempSurface, null, null
        );
    }

    private void initEncoder() {
        if (mediaCodec == null) {
            XLog.e(TAG, "VideoEncoder is not initialized");
            return;
        }
        XLog.i(TAG, "VideoEncoder configuring");
        // 注意顺序：setCallback、configure
        mediaCodec.setCallback(videoCallback);
        mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        inputSurface = mediaCodec.createInputSurface(); // 绑定到VirtualDisplay的Surface
        virtualDisplay.setSurface(inputSurface);
    }

    @Override
    public void start() {
        mediaCodec.start();
        XLog.i(TAG, "VideoEncoder started");
    }

    @Override
    public void stop() {
        stopVirtualDisplay();
        if (mediaCodec != null) {
            // 对视频编码器发送结束信号
            mediaCodec.signalEndOfInputStream();
        }
    }

    private void stopVirtualDisplay() {
        if (virtualDisplay == null) {
            return;
        }
        virtualDisplay.release();
        virtualDisplay = null;
    }

    private MediaCodec.Callback videoCallback = new MediaCodec.Callback() {
        @Override
        public void onInputBufferAvailable(MediaCodec codec, int index) {
            // 对于 Surface 输入不需要处理输入缓冲区
        }

        @Override
        public void onOutputBufferAvailable(MediaCodec codec, int index, MediaCodec.BufferInfo info) {
//            XLog.i(TAG, "Video output buffer available, index=" + index + " size=" + info.size +
//                    " pts=" + info.presentationTimeUs + " flags=" + info.flags);
            if (trackIndex == -1) {
                codec.releaseOutputBuffer(index, false);
                return;
            }
            ByteBuffer buffer = codec.getOutputBuffer(index);
            if ((info.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                // 跳过编码器的配置数据（SPS、PPS 等）
                info.size = 0;
            }
            if (buffer != null && info.size > 0) {
                buffer.position(info.offset);
                buffer.limit(info.offset + info.size);
                muxerWrapper.writeSampleData(trackIndex, buffer, info);
            }
            codec.releaseOutputBuffer(index, false);

            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                XLog.i(TAG, "End of stream reached");
            }
        }

        @Override
        public void onError(MediaCodec codec, MediaCodec.CodecException e) {
            XLog.e(TAG, "VideoEncoder error: " + e.getMessage());
        }

        @Override
        public void onOutputFormatChanged(MediaCodec codec, MediaFormat format) {
            XLog.i(TAG, "Video output format changed");
            trackIndex = muxerWrapper.addTrack(format, true);
        }
    };
}
