package com.example.andrdemocode.architecture.mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.andrdemocode.R;

public class PatternsActivity extends AppCompatActivity {

    AppViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patterns);

        TextView textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);

        viewModel = new ViewModelProvider(this).get(AppViewModel.class);

        button.setOnClickListener(view -> viewModel.getAppName());

        viewModel.appName.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String appName) {
                textView.setText(appName);
            }
        });
    }
}