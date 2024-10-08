package com.example.tennis.ui.courts

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tennis.R

import com.example.tennis.data.PlaceholderContent.PlaceholderItem
import com.example.tennis.data.SharedPreferencesHelper
import com.example.tennis.databinding.DialogCourtInfoBinding
import com.example.tennis.databinding.FragmentCourtBinding
import com.example.tennis.data.PlaceholderContent
import com.example.tennis.ui.dialog.RecyclerDialogFragment

class MyCourtRecyclerViewAdapter(
    private val places: List<PlaceholderItem>,
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

    private fun showCourtInfoDialog(item: PlaceholderItem) {
        val dialogBinding = DialogCourtInfoBinding.inflate(LayoutInflater.from(context))

        dialogBinding.tvCourtName.text = item.place
        dialogBinding.tvCourtAddress.text = item.area

        val theDetails = item.details
        dialogBinding.tvCourtPhone.text = item.details

        // 복사 아이콘 설정
        val copyDrawable = ContextCompat.getDrawable(context, R.drawable.ic_content_copy)
        dialogBinding.tvCourtPhone.setCompoundDrawablesWithIntrinsicBounds(null, null, copyDrawable, null)

        // 클릭 리스너 설정
        dialogBinding.tvCourtPhone.setOnClickListener {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("court details", theDetails)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "복사되었습니다.", Toast.LENGTH_SHORT).show()
        }

        AlertDialog.Builder(context)
            .setView(dialogBinding.root)
            .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            .show()
    }
    private fun openReservationUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
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
            } else {
                SharedPreferencesHelper.removeCourtFromStars(context, item.place)
            }
            notifyItemChanged(position)
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener {
            val itemsForSelectedPlace = PlaceholderContent.groupByPlace(item.place)

            // DialogFragment 호출 시 필터링된 아이템 리스트 전달
            val dialog = RecyclerDialogFragment.newInstance(itemsForSelectedPlace)
            dialog.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "RecyclerDialog")
        }

        holder.infoButton.setOnClickListener {
            showCourtInfoDialog(item)
        }

        holder.reserveButton.setOnClickListener {
            openReservationUrl(item.svcUrl)
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

    override fun getItemCount(): Int = places.size

    inner class ViewHolder(binding: FragmentCourtBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.textCourtName
        val areaView: TextView = binding.textCourtArea
        val starButton: ImageButton = binding.starBtn
        val imageView: ImageView = binding.courtImage
        val infoButton: TextView = binding.courtBtnInfo
        val reserveButton: TextView = binding.courtBtnReserv
        }
}