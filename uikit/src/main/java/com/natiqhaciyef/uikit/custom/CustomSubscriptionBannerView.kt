package com.natiqhaciyef.uikit.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.natiqhaciyef.uikit.databinding.CustomSubscriptionBannerViewBinding

class CustomSubscriptionBannerView(
    private val context: Context,
    attributeSet: AttributeSet
): ConstraintLayout(context, attributeSet) {
    private var binding: CustomSubscriptionBannerViewBinding? = null

    init {
        binding = CustomSubscriptionBannerViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun upgradeButtonClickAction(action: () -> Unit){
        binding?.upgradeButton?.setOnClickListener { action.invoke() }
    }
}