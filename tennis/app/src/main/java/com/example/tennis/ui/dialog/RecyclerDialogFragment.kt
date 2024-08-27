package com.example.tennis.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tennis.R
import com.example.tennis.ui.courts.placeholder.PlaceholderContent

class RecyclerDialogFragment : DialogFragment() {

    private var columnCount = 1

    lateinit var SHOW_ITEM: List<PlaceholderContent.PlaceholderItem>

//    private lateinit var itemList: ArrayList<PlaceholderContent.PlaceholderItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get arguments passed to the dialog
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set up RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.dialog_recycler_view)
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = MyItemDialogRecyclerViewAdapter(SHOW_ITEM)//PlaceholderContent.PLACE_ITEMS) //*ITEMS!
        }
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.adapter = MyItemDialogRecyclerViewAdapter(itemList)

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // Hide title

        // Set dialog dimensions to 80% of the screen
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            (resources.displayMetrics.heightPixels * 0.5).toInt()
        )

        return dialog
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(items: List<PlaceholderContent.PlaceholderItem>) =
            RecyclerDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
                SHOW_ITEM = items
            }
    }
}
