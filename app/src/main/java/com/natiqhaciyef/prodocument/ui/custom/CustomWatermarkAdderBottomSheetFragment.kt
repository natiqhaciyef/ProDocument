package com.natiqhaciyef.prodocument.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.natiqhaciyef.prodocument.databinding.FragmentCustomWatermarkAdderBottomSheetBinding
import com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.behaviour.WatermarkTypes
import com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.AddImageWatermarkFragment
import com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.AddTextWatermarkFragment
import com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.adapter.WatermarkViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CustomWatermarkAdderBottomSheetFragment(
    var cancelButtonCLickAction: () -> Unit = { },
    var continueButtonCLickAction: (String) -> Unit = { },
) : BottomSheetDialogFragment() {
    private var _binding: FragmentCustomWatermarkAdderBottomSheetBinding? = null
    private val binding: FragmentCustomWatermarkAdderBottomSheetBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentCustomWatermarkAdderBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
    }

    private fun config() {
        val fragments = listOf(
            AddTextWatermarkFragment(),
            AddImageWatermarkFragment()
        )

        val adapter =
            WatermarkViewPagerAdapter(fragments, this@CustomWatermarkAdderBottomSheetFragment)
        with(binding) {
            addWatermarkViewPager.adapter = adapter

            TabLayoutMediator(addWatermarkTabLayout, addWatermarkViewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> {
                        WatermarkTypes.ADD_TEXT_TYPE.title
                    }

                    1 -> {
                        WatermarkTypes.ADD_IMAGE_TYPE.title
                    }

                    else -> {
                        WatermarkTypes.ADD_TEXT_TYPE.title
                    }
                }
            }.attach()

            continueButton.setOnClickListener {
                val text = getTextClickAction.invoke()
                val image = getImageClickAction.invoke()

                if (text?.text != null) {
                    println(text.text)
                    continueButtonCLickAction.invoke(text.text.toString())
                }

                if(image != null){
                    println(image)
                    continueButtonCLickAction.invoke(image)
                }
            }
        }
    }

    companion object {
        var getTextClickAction: () -> TextInputEditText? = { null }
        var getImageClickAction: () -> String? = { null }
    }
}