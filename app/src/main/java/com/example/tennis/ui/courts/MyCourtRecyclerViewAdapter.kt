package com.example.tennis.ui.courts

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tennis.R

import com.example.tennis.ui.courts.placeholder.PlaceholderContent.PlaceholderItem
import com.example.tennis.databinding.FragmentCourtBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyCourtRecyclerViewAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<MyCourtRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentCourtBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentCourtBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.textCourtName
        val contentView: TextView = binding.textCourtInfo

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}