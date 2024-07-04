package com.natiqhaciyef.prodocument.ui.view.main.modify

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.natiqhaciyef.common.constants.EIGHT
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentOpenLockProtectedFileBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OpenLockProtectedFileBottomSheetFragment(
    var confirmButtonClickAction: (String) -> Unit = {}
) : BottomSheetDialogFragment() {
    private var _binding: FragmentOpenLockProtectedFileBottomSheetBinding? = null
    private val binding: FragmentOpenLockProtectedFileBottomSheetBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenLockProtectedFileBottomSheetBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
    }

    private fun config() {
        with(binding) {
            filePassword.changeVisibility()

            protectButton.setOnClickListener {
                confirmButtonClickAction.invoke(filePassword.getPasswordText())
                dialog?.dismiss()
            }
            protectedButtonEnabledCondition()
        }
    }

    private fun protectedButtonEnabledCondition() {
        with(binding) {
            filePassword.customDoOnTextChangeListener { charSequence, i, i2, i3 ->
                charSequence?.let {
                    protectButton.isEnabled = charSequence.length >= EIGHT
                }
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dialog.cancel()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dialog.cancel()
    }
}