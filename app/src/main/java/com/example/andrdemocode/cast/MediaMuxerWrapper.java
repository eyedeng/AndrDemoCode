package com.example.andrdemocode.cast;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import com.example.andrdemocode.base.XLog;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author dengyan
 * @date 2025/2/8
 * @desc 封装 MediaMuxer 的操作，管理音视频轨道添加及数据写入
 */
public class MediaMuxerWrapper {

    private static final String TAG = "MediaMuxerWrapper";

    private MediaMuxer mediaMuxer;
    private int videoTrackIndex = -1;
    private int audioTrackIndex = -1;
    private boolean muxerStarted = false;

    public MediaMuxerWrapper(String outputPath) throws IOException {
        mediaMuxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
    }

    // 添加轨道并在两个轨道都添加后启动 muxer
    public synchronized int addTrack(MediaFormat format, boolean isVideo) {
        int trackIndex = mediaMuxer.addTrack(format);
        if (isVideo) {
            videoTrackIndex = trackIndex;
        } else {
            audioTrackIndex = trackIndex;
        }
        if (videoTrackIndex != -1 && audioTrackIndex != -1 && !muxerStarted) {
            mediaMuxer.start();
            muxerStarted = true;
            XLog.i(TAG, "Muxer started, track index: " + videoTrackIndex + "," + audioTrackIndex);
        }
        return trackIndex;
    }

    // 写入编码后的数据
    public synchronized void writeSampleData(int trackIndex, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (muxerStarted) {
            mediaMuxer.writeSampleData(trackIndex, byteBuffer, bufferInfo);
        }
    }

    public synchronized void stop() {
        if (mediaMuxer != null) {
            try {
                mediaMuxer.stop();
            } catch (Exception e) {
                XLog.e(TAG, "Error stopping muxer: " + e.getMessage());
            }
            mediaMuxer.release();
            mediaMuxer = null;
            muxerStarted = false;
        }
    }
}

