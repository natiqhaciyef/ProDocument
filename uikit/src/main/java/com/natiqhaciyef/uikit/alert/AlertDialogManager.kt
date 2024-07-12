package com.natiqhaciyef.uikit.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.uikit.databinding.AlertDialogResultViewBinding

object AlertDialogManager {
    fun AppCompatActivity.createResultAlertDialog(
        title: String = EMPTY_STRING,
        description: String = EMPTY_STRING,
        buttonText: String = EMPTY_STRING,
        applyDialogDetails: (AlertDialog) -> Unit = {},
        resultClickAction: (AlertDialog) -> Unit = {}
    ) {
        val binding = AlertDialogResultViewBinding.inflate(layoutInflater)
        val resultDialog =
            AlertDialog.Builder(this, com.natiqhaciyef.common.R.style.CustomAlertDialog)
                .setView(binding.root)
                .setCancelable(true)
                .create()

        applyDialogDetails.invoke(resultDialog)

        with(binding) {
            resultButton.setOnClickListener {
                resultClickAction.invoke(resultDialog)
            }

            if (title.isNotEmpty())
                resultTitle.text = title

            if (description.isNotEmpty())
                resultDescription.text = description

            if (buttonText.isNotEmpty())
                resultButton.text = buttonText
        }

        resultDialog.show()
    }

    fun AppCompatActivity.createDynamicResultAlertDialog(
        title: String = EMPTY_STRING,
        description: String = EMPTY_STRING,
        buttonText: String = EMPTY_STRING,
        @DrawableRes resultIconId: Int = ZERO,
        successMsg: String = EMPTY_STRING,
        resultClickAction: (Dialog) -> Unit = {}
    ) {
        val binding = AlertDialogResultViewBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(binding.root)
            with(binding) {
                resultIcon.setImageResource(com.natiqhaciyef.common.R.drawable.result_dialog_container_icon)
                resultTypeImage.setImageResource(resultIconId)
                resultTitle.text = title

                resultDescription.text = description
                resultButton.text = buttonText
                resultButton.setOnClickListener {
                    resultClickAction.invoke(dialog)
                }

                window?.setFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND
                )
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                show()
            }
        }
    }
}