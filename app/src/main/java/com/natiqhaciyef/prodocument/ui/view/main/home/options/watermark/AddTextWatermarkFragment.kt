package com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.prodocument.databinding.FragmentAddTextWatermarkBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTextWatermarkFragment : Fragment() {
    private var _binding: FragmentAddTextWatermarkBinding? = null
    private val binding: FragmentAddTextWatermarkBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTextWatermarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CustomWatermarkAdderBottomSheetFragment.getTextClickAction = { binding.loginEmailInput }
    }
}