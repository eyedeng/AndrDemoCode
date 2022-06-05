package com.example.andrdemocode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.andrdemocode.databinding.ActivityMainBinding
import com.example.andrdemocode.grouprv.GroupRVActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.jump.setOnClickListener {
//            startActivity(Intent(this, MultiMusicSourceCollectionActivity::class.java))
            startActivity(Intent(this, GroupRVActivity::class.java))

        }
    }


}