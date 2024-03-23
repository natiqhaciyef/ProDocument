package com.natiqhaciyef.prodocument.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.natiqhaciyef.common.R
import com.natiqhaciyef.prodocument.databinding.CustomRememberMeButtonBinding

class CustomRememberMeButton(
    private val context: Context,
    attributeSet: AttributeSet,
) : ConstraintLayout(context, attributeSet) {
    private var binding: CustomRememberMeButtonBinding? = null
    private var isRemembered: Boolean = false

    init {
        initBinding()
    }

    private fun initBinding() {
        binding =
            CustomRememberMeButtonBinding.inflate(
                LayoutInflater.from(context),
                this,
                true
            )

    }

    fun onClickAction() {
        binding?.apply {
            rememberMeCheckBoxImage.setOnClickListener {
                isRemembered = !isRemembered
                if (isRemembered) {
                    rememberMeCheckBoxImage.setImageResource(R.drawable.selected_square_icon)
                } else {
                    rememberMeCheckBoxImage.setImageResource(R.drawable.not_selected_square_icon)
                }
            }
        }
    }

    fun detach() {
        binding = null
    }
}