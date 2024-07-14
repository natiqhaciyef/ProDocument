package com.natiqhaciyef.core.base.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.view.children
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.ZERO

class BaseXmlConstraintLayout(
    val ctx: Context,
    attributeSet: AttributeSet? = null,
) : ViewGroup(ctx, attributeSet) {
    private var baseError: BaseErrorLayout? = null

    init {
        baseError = BaseErrorLayout(context)
        if (!children.contains(baseError!!))
            addView(baseError)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var totalWidth = ZERO
        var totalHeight = ZERO

        for (i in ZERO until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            totalWidth = child.measuredWidth
            totalHeight = child.measuredHeight
        }

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(totalHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = ZERO

        for (i in ZERO until childCount) {
            val child = getChildAt(i)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            child.layout(ZERO, ZERO, childWidth, childHeight)
            currentLeft += childWidth
        }
    }

    fun successState() {
        for (child in children) {
            if (child != baseError)
                child.visibility = View.VISIBLE
        }
        baseError?.errorState(false)
        baseError?.loadingState(false)
    }

    fun errorState(isVisible: Boolean = false, isModified: Boolean = false) {
        for (child in children) {
            if (child != baseError)
                child.visibility = if (isVisible) View.GONE else View.VISIBLE
        }
        val params = baseError?.layoutParams as LayoutParams
        params.width = LayoutParams.MATCH_PARENT
        params.height = LayoutParams.MATCH_PARENT
        baseError?.errorState(isVisible, isModified)
    }

    fun loadingState(isVisible: Boolean = false) {
        for (child in children) {
            if (child != baseError)
                child.visibility = if (isVisible) View.GONE else View.VISIBLE
        }
        val params = baseError?.layoutParams as LayoutParams
        params.width = LayoutParams.MATCH_PARENT
        params.height = LayoutParams.MATCH_PARENT
        baseError?.loadingState(isVisible)
    }

    fun customizeErrorState(
        title: String = EMPTY_STRING,
        description: String = EMPTY_STRING,
        @DrawableRes icon: Int = ZERO,
        @ColorRes textColor: Int = ZERO
    ){
        baseError?.setupError(title, description, icon, textColor)
    }
}
