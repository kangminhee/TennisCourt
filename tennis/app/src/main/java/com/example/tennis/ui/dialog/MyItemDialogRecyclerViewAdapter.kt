package com.example.tennis.ui.dialog

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.tennis.data.PlaceholderContent
import com.example.tennis.databinding.FragmentItemBinding

class MyItemDialogRecyclerViewAdapter(
        private val values: List<PlaceholderContent.PlaceholderItem>)
    : RecyclerView.Adapter<MyItemDialogRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    return ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        if (item.state) holder.contentView.text = "예약 가능"
        else holder.contentView.text = "예약 중이 아님"
        holder.idView.text = item.id
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.textCourtArea
        val contentView: TextView = binding.textCourtName

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}