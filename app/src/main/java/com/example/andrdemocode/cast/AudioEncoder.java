package com.example.andrdemocode.cast;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioPlaybackCaptureConfiguration;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;

import com.example.andrdemocode.base.XLog;

import java.nio.ByteBuffer;

/**
 * 音频编码器，从 AudioRecord 中拉取数据进行编码
 */
public class AudioEncoder extends BaseEncoder {

    private final MediaProjection mediaProjection;
    private AudioRecord audioRecord;
    private AudioConfig audioConfig;


    public AudioEncoder(AudioConfig audioConfig, MediaMuxerWrapper muxer, MediaProjection mediaProjection) {
        super(audioConfig.toMediaFormat(), muxer);
        this.audioConfig = audioConfig;
        this.mediaProjection = mediaProjection;
    }

    @Override
    public void prepare() {
        initAudioRecorder();
        initEncoder();
    }

    private void initAudioRecorder() {
        AudioPlaybackCaptureConfiguration audioPlaybackCaptureConfiguration = new AudioPlaybackCaptureConfiguration.Builder(mediaProjection)
                .addMatchingUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        audioRecord = new AudioRecord.Builder()
                .setAudioFormat(new AudioFormat.Builder()
                        .setSampleRate(audioConfig.getSampleRate())
                        .setChannelMask(audioConfig.getChannelMask())
                        .setEncoding(audioConfig.getEncodeType())
                        .build())
                .setAudioPlaybackCaptureConfig(audioPlaybackCaptureConfiguration)
                .setBufferSizeInBytes(audioConfig.getBufferSize())
                .build();
    }

    private void initEncoder() {
        if (mediaCodec == null) {
            XLog.e(TAG, "AudioEncoder is not initialized");
            return;
        }
        mediaCodec.setCallback(audioCallback);
        mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
    }

    @Override
    public void start() {
        audioRecord.startRecording();
        mediaCodec.start();
        XLog.i(TAG, "AudioEncoder started");
    }

    @Override
    public void stop() {
        stopAudioRecorder();
    }

    private void stopAudioRecorder() {
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    /**
     * input ： MediaCodec 会通过getInputBuffer(int bufferId) 去拿到一个空的 ByteBuffer , 用来给客户端去填入数据(比如解码，编码的数据)，
     * MediaCodec 会用这些数据进行解码/编码处理
     * output ： MediaCodec 会把解码/编码的数据填充到一个空的 buffer 中，然后把这个填满数据的buffer给到客户端，
     * 之后需要释放这个 buffer，MediaCodec 才能继续填充数据
     * 生命周期：Stopped（Error、Uninitialized、Configured），Executing（Flushed、Running、End of Stream）和 Released
     *
     */
    private MediaCodec.Callback audioCallback = new MediaCodec.Callback() {
        @Override
        public void onInputBufferAvailable(MediaCodec codec, int index) {
            if (audioRecord == null || audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                XLog.i(TAG, "AudioRecord is not recording");
                return;
            }
            ByteBuffer inputBuffer = codec.getInputBuffer(index);
            if (inputBuffer != null) {
                int readBytes = audioRecord.read(inputBuffer, inputBuffer.capacity());
                if (readBytes > 0) {
                    codec.queueInputBuffer(index, 0, readBytes, System.nanoTime() / 1000, 0);
                }
            }
        }

        @Override
        public void onOutputBufferAvailable(MediaCodec codec, int index, MediaCodec.BufferInfo info) {
            XLog.i(TAG, "Audio output buffer available, index=" + index + " size=" + info.size +
                    " pts=" + info.presentationTimeUs + " flags=" + info.flags);
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
        }

        @Override
        public void onError(MediaCodec codec, MediaCodec.CodecException e) {
            XLog.e(TAG, "AudioEncoder error: " + e.getMessage());
        }

        @Override
        public void onOutputFormatChanged(MediaCodec codec, MediaFormat format) {
            XLog.i(TAG, "Audio output format changed");
            trackIndex = muxerWrapper.addTrack(format, false);
        }
    };
}
