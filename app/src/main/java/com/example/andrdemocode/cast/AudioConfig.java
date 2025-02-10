package com.example.andrdemocode.cast;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;

import com.example.andrdemocode.base.XLog;

/**
 * @author dengyan
 * @date 2025/1/21
 * @desc 封装音频相关配置参数
 */
public class AudioConfig {
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    private int bitRate;
    private int sampleRate;
    private int channelCount;
    private String mimeType;
    private int profile;
    private int encodeType;
    private int bufferSize = DEFAULT_BUFFER_SIZE;

    public AudioConfig(int bitRate, int sampleRate, int channelCount) {
        this.bitRate = bitRate;
        this.sampleRate = sampleRate;
        this.channelCount = channelCount;
        this.mimeType = MediaFormat.MIMETYPE_AUDIO_AAC;
        this.profile = MediaCodecInfo.CodecProfileLevel.AACObjectLC;
        this.encodeType = AudioFormat.ENCODING_PCM_16BIT;
    }

    public int getBitRate() {
        return bitRate;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getChannelCount() {
        return channelCount;
    }

    public int getChannelMask() {
        return channelCount == 1 ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO;
    }

    public String getMimeType() {
        return mimeType;
    }

    public int getProfile() {
        return profile;
    }

    public int getBufferSize() {
        bufferSize = AudioRecord.getMinBufferSize(sampleRate, getChannelMask(), encodeType);
        XLog.i("AudioConfig", "getBufferSize: " + bufferSize);
        return bufferSize;
    }

    public int getEncodeType() {
        return encodeType;
    }

    public MediaFormat toMediaFormat() {
        MediaFormat format = MediaFormat.createAudioFormat(mimeType, sampleRate, channelCount);
        format.setInteger(MediaFormat.KEY_AAC_PROFILE, profile);
        format.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
        return format;
    }
}
