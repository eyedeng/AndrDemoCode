package com.example.andrdemocode.cast;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Surface;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.andrdemocode.base.XLog;
import com.example.andrdemocode.databinding.ActivityScreenCaptureBinding;
import com.example.andrdemocode.utils.TimeUtil;

/**
 * 屏幕+声音录制
 */
public class ScreenCaptureActivity extends AppCompatActivity {
    private static final String TAG = "ScreenCaptureActivity";
    private ActivityScreenCaptureBinding binding;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private Surface surface;
    private DisplayConfig displayConfig;
    private DisplayConfig.Builder displayConfigBuilder;
    private VideoEncoder videoEncoder;
    private AudioConfig audioConfig;
    private AudioEncoder audioEncoder;
    private MediaMuxerWrapper muxerWrapper;

    private int resultCode;
    private Intent resultData;

    private final ActivityResultLauncher<Intent> screenCaptureLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() != Activity.RESULT_OK) {
                    XLog.i(TAG, "User cancelled");
                    return;
                }
                resultCode = result.getResultCode();
                resultData = result.getData();
                startCapture();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScreenCaptureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toggleMirror.setOnClickListener(v -> toggleCapture(true));
        binding.toggleSub.setOnClickListener(v -> toggleCapture(false));
        // 输入编码器前，显示在SurfaceView上
        surface = binding.surface.getHolder().getSurface();

        initMediaProjectionManager();
        initDisplayConfig();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // todo 屏幕尺寸变化
    }

    private void initMediaProjectionManager() {
        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
    }

    private void initDisplayConfig() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayConfigBuilder = new DisplayConfig.Builder()
                .setWidth(displayMetrics.widthPixels)
                .setHeight(displayMetrics.heightPixels)
                .setDensity(displayMetrics.densityDpi);
    }

    private void toggleCapture(boolean mirror) {
        displayConfig = displayConfigBuilder.setMirrorMode(mirror ? 0 : 1)
                .build();
        if (videoEncoder == null) {
            // 启动录制授权请求
            Intent captureIntent = mediaProjectionManager.createScreenCaptureIntent();
            screenCaptureLauncher.launch(captureIntent);
        } else {
            stopCapture();
        }
    }

    private void startCapture() {
        XLog.i(TAG, "Starting");
        initMediaProjection();
        initMuxer();
        videoEncoder = new VideoEncoder(displayConfig, muxerWrapper, mediaProjection, surface);
        videoEncoder.prepare();
        videoEncoder.start();

        audioConfig = new AudioConfig(128000, 44100, 1);
        audioEncoder = new AudioEncoder(audioConfig, muxerWrapper, mediaProjection);
        audioEncoder.prepare();
        audioEncoder.start();

        updateToggleButton(true);
    }

    private void initMediaProjection() {
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, resultData);
    }

    // 初始化 MediaMuxerWrapper，用于文件输出
    private void initMuxer() {
        String outputPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + TimeUtil.getFormattedTime() + "-ScreenCapture.mp4";
        try {
            muxerWrapper = new MediaMuxerWrapper(outputPath);
        } catch (Exception e) {
            XLog.e(TAG, "Failed to create MediaMuxerWrapper: " + e.getMessage());
        }
    }


    private void stopCapture() {
        XLog.i(TAG, "Stopping");
        if (videoEncoder != null) {
            videoEncoder.stop();
            videoEncoder.release();
            videoEncoder = null;
        }
        if (audioEncoder != null) {
            audioEncoder.stop();
            audioEncoder.release();
            audioEncoder = null;
        }
        if (muxerWrapper != null) {
            muxerWrapper.stop();
        }
        updateToggleButton(false);
    }

    private void updateToggleButton(boolean recording) {
        String text = recording ? "end" : "start";
        if (displayConfig.isMirror()) {
            binding.toggleMirror.setText(text);
        } else {
            binding.toggleSub.setText(text);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCapture();
        if (mediaProjection != null) {
            mediaProjection.stop();
            mediaProjection = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // todo 显示点按操作反馈
        int action = event.getAction();
        int actionMasked = event.getActionMasked(); // action & 0xff
        XLog.i(TAG, "Action: " + actionMasked + " (" + action + ")");
        int pointerCount = event.getPointerCount();  // 手指数量
        for (int i = 0; i < pointerCount; i++) {
            MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
            event.getPointerCoords(i, pointerCoords);
            XLog.i(TAG, "Pointer " + i + ": " + pointerCoords.x + "x" + pointerCoords.y + " (" + pointerCoords.pressure + ")" + " (" + pointerCoords.size + ")");
        }
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:         // 按下
            case MotionEvent.ACTION_POINTER_DOWN: // 非第一个手指按下
                XLog.i(TAG, "Pointer down");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                XLog.i(TAG, "Pointer up");
                break;
            case MotionEvent.ACTION_MOVE:
                XLog.i(TAG, "Pointer move");
                break;
        }
        return true;
    }
}