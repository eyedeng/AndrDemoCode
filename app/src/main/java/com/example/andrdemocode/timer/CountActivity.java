package com.example.andrdemocode.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.example.andrdemocode.R;

public class CountActivity extends AppCompatActivity {
    private static final String TAG = "CountActivity";
    private TextView textView;
    CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long l) {
                Log.i(TAG, "onTick: " + l);
//                new Thread(() -> {
//                    try {
//                        Thread.sleep(3);
//                        Log.i(TAG, "sleeped: ");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }).start();
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: ");
            }
        };

        findViewById(R.id.start).setOnClickListener(view -> {
            timer.start();
        });

        findViewById(R.id.cancel).setOnClickListener(view -> {
            timer.cancel();
        });

    }
}