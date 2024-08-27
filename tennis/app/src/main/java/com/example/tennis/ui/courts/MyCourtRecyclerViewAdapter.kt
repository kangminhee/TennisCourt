
package com.example.tennis.ui.courts

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.example.tennis.R

import com.example.tennis.ui.courts.placeholder.PlaceholderContent.PlaceholderItem
import com.example.tennis.data.SharedPreferencesHelper
import com.example.tennis.databinding.DialogCourtInfoBinding
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
            notifyDataSetChanged()
        }
        holder.infoButton.setOnClickListener {
            showCourtInfoDialog(item)
        }

        holder.reserveButton.setOnClickListener {
            openReservationUrl(item.svcUrl)
        }
    }

    override fun getItemCount(): Int = places.size

    inner class ViewHolder(binding: FragmentCourtBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.textCourtName
        val areaView: TextView = binding.textCourtArea
        val starButton: ImageButton = binding.starBtn
        val infoButton: TextView = binding.courtBtnInfo
        val reserveButton: TextView = binding.courtBtnReserv

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
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
}