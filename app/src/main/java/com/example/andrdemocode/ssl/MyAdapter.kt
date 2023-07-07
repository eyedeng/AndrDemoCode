package com.example.andrdemocode.ssl

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.andrdemocode.R

/**
 * RecyclerView列表单选：`selectedPosition`，用于保存当前选中项的位置，点击下一项时，notifyItemChanged刷新这两项
 */
class MyAdapter(private val dataList: List<MyDataModel>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = dataList[position]
        holder.textView.text = dataModel.text
        holder.itemView.isSelected = dataModel.isSelected
        if (dataModel.isSelected) {
            holder.textView.setTextColor(Color.RED)
        } else {
            holder.textView.setTextColor(0xFF005500.toInt())
        }

        holder.itemView.setOnClickListener {
            if (selectedPosition != position) {
                if (selectedPosition != -1) {
                    dataList[selectedPosition].isSelected = false
                    notifyItemChanged(selectedPosition)
                }
                selectedPosition = position
                dataList[selectedPosition].isSelected = true
                notifyItemChanged(selectedPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun getSelectedPosition(): Int {
        return selectedPosition
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.itemTextView)
    }
}
