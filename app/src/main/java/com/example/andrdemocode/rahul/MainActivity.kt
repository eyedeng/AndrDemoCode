package com.example.andrdemocode.rahul

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.andrdemocode.databinding.ActivityMain2Binding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val contacts = mutableListOf<Contact>()
        val contactAdapter = ContactAdapter(this, contacts)
        binding.rvContacts.apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        val model = ViewModelProvider(this)[MainActivityViewModel::class.java]

    }


}