package com.example.andrdemocode

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.andrdemocode.databinding.ActivityMainBinding
import com.example.andrdemocode.grouprv.GroupRVActivity
import com.example.andrdemocode.provider.MyDBHelper
import com.example.andrdemocode.storage.StorageActivity
import com.example.andrdemocode.tabview.MultiMusicSourceCollectionActivity
import com.example.andrdemocode.timer.CountActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var funcAdapter: FuncAdapter
    private val dataSet = mutableListOf<Item>()

    companion object {
        const val DEST_GROUP = 1
        const val DEST_TAB = 2
        const val DEST_COUNT = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.jump.setOnClickListener {
//            startActivity(Intent(this, MultiMusicSourceCollectionActivity::class.java))
//            startActivity(Intent(this, GroupRVActivity::class.java))
            startActivity(Intent(this, CountActivity::class.java))

        }

        initData()
        intView()
    }

    private fun initData() {
        dataSet.add(Item("DEST_GROUP", DEST_GROUP))
        dataSet.add(Item("DEST_TAB", DEST_TAB))
        dataSet.add(Item("DEST_COUNT", DEST_COUNT))
    }

    private fun intView() {
        funcAdapter = FuncAdapter(dataSet)
        binding.recycleView.apply {
            adapter = funcAdapter
        }

    }

    class FuncAdapter(val dataSet: List<Item>) : RecyclerView.Adapter<FuncVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuncVH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.main_function_item, parent, false)
            return FuncVH(view)
        }

        override fun onBindViewHolder(holder: FuncVH, position: Int) {
            holder.bind(dataSet[position])
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }

    }

    class FuncVH(view: View) : RecyclerView.ViewHolder(view) {
        val descTv: TextView

        init {
            descTv = view.findViewById(R.id.desc_tv)
        }

        fun bind(data: Item) {
            descTv.text = data.desc
            itemView.setOnClickListener {data.onClick(it.context)}
        }
    }

    data class Item(val desc: String, val dest: Int) {
        fun onClick(context: Context) {
            when (dest) {
                DEST_GROUP -> context.startActivity(Intent(context, GroupRVActivity::class.java))
                DEST_TAB -> context.startActivity(Intent(context, MultiMusicSourceCollectionActivity::class.java))
                DEST_COUNT -> context.startActivity(Intent(context, CountActivity::class.java))
            }
        }
    }

    private fun cp() {
        // 数据库
        val dbHelper = MyDBHelper(this, 1)
        val database = dbHelper.readableDatabase

        // CP
        val uri = Uri.parse("content://${packageName}.provider/table1")
        val cursor = contentResolver.query(uri, arrayOf("_id", "name"), "name = ?", arrayOf("dy"), "name ASC")

    }
}