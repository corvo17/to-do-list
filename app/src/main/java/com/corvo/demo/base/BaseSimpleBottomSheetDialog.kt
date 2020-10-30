package com.corvo.demo.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.corvo.demo.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.list_base_bottom_sheet.view.*

class BaseSimpleBottomSheetDialog : BottomSheetDialogFragment() {


    var items : List<String>? = null
    var title : String = ""
    var clicked : ((Int) -> Unit)? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.list_base_bottom_sheet,container,false)

        view.title?.text = title
        view.bottom_card_list?.layoutManager = LinearLayoutManager(activity)
        view.bottom_card_list?.setHasFixedSize(true)

        val adapter = BaseSimpleListAdapter(activity!!,items ?: arrayListOf())
        adapter?.clicked = clicked
        view.bottom_card_list?.adapter = adapter


        return view
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,R.style.BottomSheetDialog)
    }


}