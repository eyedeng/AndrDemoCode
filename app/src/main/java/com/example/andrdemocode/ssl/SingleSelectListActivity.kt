package com.example.andrdemocode.ssl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.andrdemocode.databinding.ActivitySingleSelectListBinding

class SingleSelectListActivity : AppCompatActivity() {

    lateinit var binding: ActivitySingleSelectListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleSelectListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        val dataList = ArrayList<MyDataModel>()
        (1..10).forEach {
            dataList.add(MyDataModel("$it"))
        }
        val adapter = MyAdapter(dataList)
        binding.rv.adapter = adapter
        binding.confirm.setOnClickListener {
            Log.i("TAG", "confirm: ${adapter.getSelectedPosition()}")
        }

    }
}