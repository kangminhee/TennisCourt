package com.example.tennis.ui.dialog

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.tennis.R
import com.example.tennis.data.PlaceholderContent
import com.example.tennis.databinding.FragmentItemBinding
import java.io.IOException

class MyItemDialogRecyclerViewAdapter(
        private val values: List<PlaceholderContent.PlaceholderItem>)
    : RecyclerView.Adapter<MyItemDialogRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    return ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        if (item.state) {
            holder.contentView.text = "예약 가능"
        } else {
            holder.contentView.text = "예약 중이 아님"
        }

        val imageFileName = "s" + item.id.removePrefix("S") //+ ".jpg"
        holder.idView.text = imageFileName //item.svcname

        // item.id에서 'S' 제거 (예: "S132312" -> "132312")

        try {
            // assets에서 이미지 파일을 불러와서 Drawable로 변환
            val inputStream = holder.itemView.context.assets.open(imageFileName)
            val drawable = Drawable.createFromStream(inputStream, null)

            // 이미지 설정
            holder.imageView.setImageDrawable(drawable)

            // 스트림 닫기
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            // 이미지 로드 실패 시 기본 이미지 설정
            holder.imageView.setImageResource(R.drawable.red_rounded_box)
        }
    }


//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = values[position]
//        if (item.state) holder.contentView.text = "예약 가능"
//        else holder.contentView.text = "예약 중이 아님"
//        holder.idView.text = item.svcname
//
//        val context = holder.itemView.context
//        val resourceId = context.resources.getIdentifier(item.id, "drawable", context.packageName)
//
//        try {
//            if (resourceId != 0) {
//                // 리소스 ID가 유효할 경우 이미지 설정
//                holder.imageView.setImageResource(resourceId)
//            } else {
//                // 리소스 ID가 유효하지 않을 경우 예외 처리
//                throw IOException("Resource not found")
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//            // 이미지 로드 실패 시 기본 이미지 설정
//            holder.imageView.setImageResource(R.drawable.red_rounded_box)
//        }
//    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.textCourtArea
        val contentView: TextView = binding.textCourtName
        val imageView: ImageView = binding.courtImage

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}