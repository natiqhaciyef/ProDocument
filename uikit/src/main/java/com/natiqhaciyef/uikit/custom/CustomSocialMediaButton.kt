package com.natiqhaciyef.uikit.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.natiqhaciyef.uikit.databinding.CustomSocialPlatformButtonBinding

class CustomSocialMediaButton(
    private val context: Context,
    attributeSet: AttributeSet,
) : ConstraintLayout(context, attributeSet) {
    private var binding: CustomSocialPlatformButtonBinding? = null

    init {
        initBinding()
    }

    private fun initBinding() {
        binding =
            CustomSocialPlatformButtonBinding.inflate(
                LayoutInflater.from(context),
                this,
                true
            )

    }

    fun setIconImage(resImageId: Int) {
        binding?.apply {
            socialPlatformImage.setImageResource(resImageId)
        }
    }

    fun setTitleText(text: String) {
        binding?.apply {
            socialPlatformTitle.text = text
        }
    }

    fun onClickButtonAction(listener: () -> Unit) {
        binding?.socialPlatformButton?.setOnClickListener {
            listener()
        }
    }

    fun changeTextVisibility(visible: Int) {
        binding?.socialPlatformTitle?.visibility = visible
    }

    fun changeImageHorizontalBias(bias: Float) {
        binding?.apply {
            val yourView = socialPlatformImage
            val constraintSet = ConstraintSet()
            constraintSet.clone(platformName)
            constraintSet.setHorizontalBias(yourView.id, bias)
            constraintSet.applyTo(platformName)
        }
    }

    fun detach() {
        binding = null
    }
}