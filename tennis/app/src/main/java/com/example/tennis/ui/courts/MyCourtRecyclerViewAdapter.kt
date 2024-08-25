package com.example.tennis.ui.courts

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.tennis.R
import com.example.tennis.data.SharedPreferencesHelper
import com.example.tennis.ui.courts.placeholder.PlaceholderContent.PlaceholderItem
import com.example.tennis.databinding.FragmentCourtBinding

class MyCourtRecyclerViewAdapter(
    private val values: List<PlaceholderItem>,
    private val context: Context
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

        val isStarred = SharedPreferencesHelper.isCourtStarred(context, item.id)
        holder.starButton.setImageResource(if (isStarred) R.drawable.ic_notifications_black_24dp else R.drawable.ic_notifications_white_24dp)

        // 별 모양 버튼 클릭 시 즐겨찾기 추가/제거 로직
        holder.starButton.setOnClickListener {
            if (isStarred) {
                SharedPreferencesHelper.removeCourtFromStars(context, item.id)
                holder.starButton.setImageResource(R.drawable.ic_notifications_black_24dp)
            } else {
                SharedPreferencesHelper.addCourtToStars(context, item.id)
                holder.starButton.setImageResource(R.drawable.ic_notifications_white_24dp)
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentCourtBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.textCourtName
        val contentView: TextView = binding.textCourtInfo
        val starButton: ImageButton = binding.starBtn

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}
