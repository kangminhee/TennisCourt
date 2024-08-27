package com.example.tennis.ui.starred

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tennis.R
import com.example.tennis.data.SharedPreferencesHelper
import com.example.tennis.data.PlaceholderContent
import com.example.tennis.databinding.FragmentStarredBinding
import com.example.tennis.ui.dialog.RecyclerDialogFragment

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
            } else {
                SharedPreferencesHelper.removeCourtFromStars(context, item.place)
            }
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            val itemsForSelectedPlace = PlaceholderContent.groupByPlace(item.place)

            // DialogFragment 호출 시 필터링된 아이템 리스트 전달
            val dialog = RecyclerDialogFragment.newInstance(itemsForSelectedPlace)
            dialog.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "RecyclerDialog")
        }

        // item.id에서 'S' 제거 (예: "S1234" -> "1234")
        val imageFileName = "s" + item.id.removePrefix("S") // 예: "s1234"

        // 이미지 리소스를 drawable에서 불러오기
        val imageResId = holder.itemView.context.resources.getIdentifier(imageFileName, "drawable", holder.itemView.context.packageName)

        if (imageResId != 0) {
            // 리소스 ID가 유효한 경우 이미지 설정
            holder.imageView.setImageResource(imageResId)
        } else {
            // 이미지 리소스를 찾을 수 없는 경우 기본 이미지 설정
            holder.imageView.setImageResource(R.drawable.red_rounded_box)
        }
    }


    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentStarredBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.textCourtName
        val areaView: TextView = binding.textCourtArea
        val starButton: ImageButton = binding.starBtn
        val imageView: ImageView = binding.courtImage

        override fun toString(): String {
            return super.toString() + " '" + areaView.text + "'"
        }
    }

}
