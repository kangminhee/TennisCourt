package com.example.tennis.ui.courts

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tennis.R
import com.example.tennis.data.PlaceholderContent
import com.example.tennis.data.PlaceholderContent.PLACE_ITEMS

import com.example.tennis.data.PlaceholderContent.PlaceholderItem
import com.example.tennis.data.SharedPreferencesHelper
import com.example.tennis.databinding.FragmentCourtBinding
import com.example.tennis.ui.dialog.RecyclerDialogFragment

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
            notifyDataSetChanged()
        }

//        Intent browserIntent = new Intent(i)

        holder.itemView.setOnClickListener {
            val itemsForSelectedPlace = PlaceholderContent.groupByPlace(item.place)

            // DialogFragment 호출 시 필터링된 아이템 리스트 전달
            val dialog = RecyclerDialogFragment.newInstance(itemsForSelectedPlace)
            dialog.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "RecyclerDialog")
        }

//        holder.itemView.setOnClickListener {
//            val filteredItems = ITEMS.filter { it.place == item.place }
//            // 아이템 클릭 시 DialogFragment 호출
//            val dialog = RecyclerDialogFragment.newInstance(ArrayList(filteredItems))
//            dialog.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "RecyclerDialog")
//        }
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