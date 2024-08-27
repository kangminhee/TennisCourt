package com.example.tennis.ui.starred

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.tennis.R
import com.example.tennis.data.SharedPreferencesHelper
import com.example.tennis.ui.courts.placeholder.PlaceholderContent
import com.example.tennis.databinding.FragmentStarredBinding

class MyStarredCourtRecyclerViewAdapter(
    private val values: List<PlaceholderContent.PlaceholderItem>,
    private val context: Context
) : RecyclerView.Adapter<MyStarredCourtRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentStarredBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.nameView.text = item.place
        holder.areaView.text = item.area

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
//            notifyDataSetChanged()
        }
    }


    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentStarredBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.itemNumber
        val areaView: TextView = binding.content
        val starButton: ImageButton = binding.starBtn

        override fun toString(): String {
            return super.toString() + " '" + areaView.text + "'"
        }
    }

}
