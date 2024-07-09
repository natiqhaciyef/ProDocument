package com.natiqhaciyef.prodocument.ui.view.main.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.common.constants.EIGHT
import com.natiqhaciyef.core.base.ui.BaseBottomSheetFragment
import com.natiqhaciyef.prodocument.databinding.FragmentOpenLockProtectedFileBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OpenLockProtectedFileBottomSheetFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOpenLockProtectedFileBottomSheetBinding =
        FragmentOpenLockProtectedFileBottomSheetBinding::inflate,
    override var onItemClickAction: (String) -> Unit = {},
) : BaseBottomSheetFragment<FragmentOpenLockProtectedFileBottomSheetBinding, String>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
    }

    private fun config() {
        with(binding) {
            filePassword.changeVisibility()

            protectButton.setOnClickListener {
                onItemClickAction.invoke(filePassword.getPasswordText())
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
}