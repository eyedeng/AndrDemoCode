package com.example.andrdemocode.rahul

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.andrdemocode.R

/**
 * @author dengyan
 * @date 2022/10/29
 * @desc
 */
class ContactAdapter(private val context: Context, private val contacts: List<Contact>) : RecyclerView.Adapter<ContactAdapter.VH>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount() = contacts.size

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView
        private val age: TextView
        private val profile: ImageView

        init {
            name = itemView.findViewById(R.id.tvName)
            age = itemView.findViewById(R.id.tvAge)
            profile = itemView.findViewById(R.id.ivProfile)
        }
        fun bind(contact: Contact) {
            name.text = contact.name
            age.text = "Age: ${contact.age}"
            Glide.with(context).load(contact.imageUrl).into(profile)
        }
    }

}