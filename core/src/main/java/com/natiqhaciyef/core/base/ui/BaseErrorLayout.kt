package com.natiqhaciyef.core.base.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.R
import com.natiqhaciyef.core.databinding.BaseErrorLayoutBinding

class BaseErrorLayout(
    val ctx: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(ctx, attributeSet) {
    var binding: BaseErrorLayoutBinding? = null

    init {
        binding = BaseErrorLayoutBinding.inflate(LayoutInflater.from(ctx), this, true)
    }

    fun setupError(
        title: String = EMPTY_STRING,
        description: String = EMPTY_STRING,
        @DrawableRes icon: Int = ZERO,
        @ColorRes textColor: Int = ZERO
    ) {
        binding?.apply {
            if (icon != ZERO)
                errorImage.setImageResource(icon)

            if (description != EMPTY_STRING)
                errorDescription.text =
                    ctx.getString(com.natiqhaciyef.common.R.string.files_loading_error_description_result)

            if (title != EMPTY_STRING)
                errorTitle.text = SOMETHING_WENT_WRONG

            if (textColor != ZERO) {
                errorTitle.setTextColor(textColor)
                errorDescription.setTextColor(textColor)
            }
        }
    }

    fun errorState(isVisible: Boolean = false, isModified: Boolean = false) {
        binding?.apply {
            errorLayoutHolder.visibility = if (isVisible) View.VISIBLE else View.GONE

            if (!isModified)
                setupError(
                    title = SOMETHING_WENT_WRONG,
                    description = ctx.getString(R.string.files_loading_error_description_result),
                    icon = R.drawable.material_not_found_icon
                )
            if (isVisible) progressBarLayout.visibility = View.GONE
        }
    }

    fun loadingState(isVisible: Boolean = false) {
        binding?.apply {
            if (isVisible) errorLayoutHolder.visibility = View.GONE
            progressBarLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }
}