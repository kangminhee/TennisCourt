package com.example.tennis.ui.starred

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.tennis.R
import com.example.tennis.data.SharedPreferencesHelper
import com.example.tennis.databinding.DialogCourtInfoBinding
import com.example.tennis.ui.courts.placeholder.PlaceholderContent
import com.example.tennis.databinding.FragmentStarredBinding
import com.example.tennis.ui.courts.MyCourtRecyclerViewAdapter

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

        val isStarred = SharedPreferencesHelper.isCourtStarred(context, item.id)
        holder.starButton.setImageResource(if (isStarred) R.drawable.ic_notifications_black_24dp else R.drawable.ic_notifications_white_24dp)

        // 별 모양 버튼 클릭 시 즐겨찾기 추가/제거 로직
        holder.starButton.setOnClickListener {
            val newStarredState = !isStarred

            if (newStarredState) {
                SharedPreferencesHelper.addCourtToStars(context, item.id)
//                holder.starButton.setImageResource(R.drawable.ic_notifications_black_24dp)
            } else {
                SharedPreferencesHelper.removeCourtFromStars(context, item.id)
//                holder.starButton.setImageResource(R.drawable.ic_notifications_white_24dp)
            }
            notifyItemChanged(position)
//            notifyDataSetChanged()
        }
        // 시설 정보 버튼 클릭 리스너
        holder.infoButton.setOnClickListener {
            showCourtInfoDialog(item)
        }

        // 예약 사이트로 이동 버튼 클릭 리스너
        holder.reserveButton.setOnClickListener {
            openReservationUrl(item.svcUrl)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentStarredBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.itemNumber
        val areaView: TextView = binding.content
        val starButton: ImageButton = binding.starBtn
        val infoButton: TextView = binding.courtBtnInfo
        val reserveButton: TextView = binding.courtBtnReserv

        override fun toString(): String {
            return super.toString() + " '" + areaView.text + "'"
        }
    }
        private fun showCourtInfoDialog(item: PlaceholderContent.PlaceholderItem) {
            val dialogBinding = DialogCourtInfoBinding.inflate(LayoutInflater.from(context))

            dialogBinding.tvCourtName.text = item.place
            dialogBinding.tvCourtAddress.text = item.area

            val phoneNumber = item.details
            dialogBinding.tvCourtPhone.text = phoneNumber

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
}
