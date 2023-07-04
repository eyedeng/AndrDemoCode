package com.example.andrdemocode.lauchmode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.andrdemocode.databinding.ActivityAactivityBinding

class AActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.loadUrl("https://www.baidu.com")

    }
}