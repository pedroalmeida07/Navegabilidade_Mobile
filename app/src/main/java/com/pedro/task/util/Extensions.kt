package com.pedro.task.util

import android.os.Message
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pedro.task.R
import com.pedro.task.databinding.BottomSheetBinding

fun Fragment.initToolbar(toolbar: Toolbar){
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title=""
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.setNavigationOnClickListener {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}

fun Fragment.showBottomSheet(
    titleDialog: Int? = null,
    titleButton: Int? = null,
    message: Int,
    onClick: () -> Unit ={}
){
    val bottomSheetDialog = BottomSheetDialog(requireContext())
    val binding: BottomSheetBinding = BottomSheetBinding.inflate(layoutInflater, null, false)

    binding.textViewTitle.text = getText(titleDialog ?: R.string.text_title_warning)
    binding.textViewMessage.text = getText(message)
    binding.buttonOk.text = getText(titleButton ?: R.string.text_button_warning)
    binding.buttonOk.setOnClickListener {
        onClick()
        bottomSheetDialog.dismiss()
    }
}