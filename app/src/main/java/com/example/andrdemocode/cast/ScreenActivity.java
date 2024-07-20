package com.example.andrdemocode.cast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.widget.Toast;

import com.example.andrdemocode.base.XLog;
import com.example.andrdemocode.databinding.ActivityScreenBinding;

public class ScreenActivity extends AppCompatActivity {
    private static final String TAG = "ScreenActivity";
    private ActivityScreenBinding binding;
    private OrientationEventListener orientationEventListener;
    private int lastOrientation = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonPortrait.setOnClickListener(v -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));
        binding.buttonLandscape.setOnClickListener(v -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE));
        binding.buttonReversePortrait.setOnClickListener(v -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT));
        binding.buttonReverseLandscape.setOnClickListener(v -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE));
        binding.buttonSensor.setOnClickListener(v -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR));
        binding.sensorLand.setOnClickListener(v -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE));
        binding.user.setOnClickListener(v -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER));

        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    return;
                }
                int rotation = getDisplay().getRotation();
                if (orientation % 10 == 0) {
                    XLog.i(TAG, "onOrientationChanged: " + orientation + " rotation: " + rotation);
                }
                int newOrientation;
                if ((orientation >= 0 && orientation < 45) || (orientation >= 315 && orientation < 360)) {
                    newOrientation = 0;
                } else if (orientation >= 225 && orientation < 315) {
                    newOrientation = 270;
                } else if (orientation >= 45 && orientation < 135) {
                    newOrientation = 90;
                } else {
                    return;
                }

                if (newOrientation != lastOrientation) {
                    lastOrientation = newOrientation;
                    switch (newOrientation) {
                        case 0:
                            Toast.makeText(ScreenActivity.this, "竖屏（正常方向）", Toast.LENGTH_SHORT).show();
                            break;
                        case 90:
                            Toast.makeText(ScreenActivity.this, "横屏（正常方向）", Toast.LENGTH_SHORT).show();
                            break;
                        case 270:
                            Toast.makeText(ScreenActivity.this, "横屏（反方向）", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        };
        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
        } else {
            orientationEventListener.disable();
            XLog.i(TAG, "onCreate: can not detect orientation");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(orientationEventListener != null) {
            orientationEventListener.disable();
        }
    }
}