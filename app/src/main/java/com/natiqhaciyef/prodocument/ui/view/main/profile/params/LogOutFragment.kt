package com.natiqhaciyef.prodocument.ui.view.main.profile.params

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentLogOutBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LogOutFragment(
    private var onYesClick: () -> Unit = {},
    private var onCancelClick: () -> Unit = {}
) : BottomSheetDialogFragment() {
    private var _binding: FragmentLogOutBinding? = null
    private val binding: FragmentLogOutBinding get() =  _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogOutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            yesButton.setOnClickListener { onYesClick.invoke() }
            cancelButton.setOnClickListener { onCancelClick.invoke() }
        }
    }

}