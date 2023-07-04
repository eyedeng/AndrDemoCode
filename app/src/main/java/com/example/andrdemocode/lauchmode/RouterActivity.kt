package com.example.andrdemocode.lauchmode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.andrdemocode.R

/**
 * H5跳转，见router.html
 */
class RouterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_router)

        val url = intent.data
        Log.i("TAG", "onCreate: ${url.toString()}")
    }
}
