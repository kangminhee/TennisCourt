package com.example.tennis.ui.courts

import android.content.Intent
import android.net.Uri

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.tennis.R

import com.example.tennis.ui.courts.placeholder.PlaceholderContent.PlaceholderItem
import com.example.tennis.data.SharedPreferencesHelper
import com.example.tennis.databinding.FragmentCourtBinding

class MyCourtRecyclerViewAdapter(
    private val places: List<PlaceholderItem>,
    private val context: Context
//    private val onItemClick: (String) -> Unit
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
        val item = places[position]
        holder.nameView.text = item.place
        holder.areaView.text = item.id

        val isStarred = SharedPreferencesHelper.isCourtStarred(context, item.place)
        holder.starButton.setImageResource(if (isStarred) R.drawable.ic_notifications_black_24dp else R.drawable.ic_notifications_white_24dp)

        // 별 모양 버튼 클릭 시 즐겨찾기 추가/제거 로직
        holder.starButton.setOnClickListener {
            val newStarredState = !isStarred

            if (newStarredState) {
                SharedPreferencesHelper.addCourtToStars(context, item.place)
//                holder.starButton.setImageResource(R.drawable.ic_notifications_black_24dp)
            } else {
                SharedPreferencesHelper.removeCourtFromStars(context, item.place)
//                holder.starButton.setImageResource(R.drawable.ic_notifications_white_24dp)
            }
            notifyItemChanged(position)
            notifyDataSetChanged()
        }

//        Intent browserIntent = new Intent(i)
    }

    override fun getItemCount(): Int = places.size

    inner class ViewHolder(binding: FragmentCourtBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.textCourtName
        val areaView: TextView = binding.textCourtArea
        val starButton: ImageButton = binding.starBtn
        val reserveButton: Button = binding.courtBtnReserv


//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }

}