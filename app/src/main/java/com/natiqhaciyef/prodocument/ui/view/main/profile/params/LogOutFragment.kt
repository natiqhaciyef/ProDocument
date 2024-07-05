package com.natiqhaciyef.prodocument.ui.view.main.profile.params

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.natiqhaciyef.core.base.ui.BaseBottomSheetFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentLogOutBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LogOutFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLogOutBinding =
        FragmentLogOutBinding::inflate,
    private var onYesClick: () -> Unit = {},
) : BaseBottomSheetFragment<FragmentLogOutBinding, Unit>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            yesButton.setOnClickListener { onYesClick.invoke() }
            cancelButton.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }

}