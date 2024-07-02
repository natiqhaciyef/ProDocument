package com.natiqhaciyef.uikit.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.natiqhaciyef.uikit.databinding.AlertDialogResultViewBinding

object AlertDialogManager {
    fun AppCompatActivity.createResultAlertDialog(
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

        binding.resultButton.setOnClickListener {
            resultClickAction.invoke(resultDialog)
        }

        resultDialog.show()
    }

    fun AppCompatActivity.createDynamicResultAlertDialog(
        resultIcon: Int, successMsg: String, description: String,
        resultClickAction: (Dialog) -> Unit = {}
    ) {
        val binding = AlertDialogResultViewBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(binding.root)

            binding.resultIcon.setImageResource(com.natiqhaciyef.common.R.drawable.result_dialog_container_icon)
            binding.resultTypeImage.setImageResource(resultIcon)
            binding.resultTitle.text = getString(
                com.natiqhaciyef.common.R.string.change_password_alert_dialog_title,
                successMsg.lowercase()
            )

            binding.resultDescription.text = description

            binding.resultButton.setOnClickListener {
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