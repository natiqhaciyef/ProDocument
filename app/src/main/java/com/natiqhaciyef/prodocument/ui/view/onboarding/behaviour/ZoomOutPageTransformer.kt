package com.natiqhaciyef.prodocument.ui.view.onboarding.behaviour

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.TWO
import com.natiqhaciyef.common.constants.ZERO


class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    companion object {
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
    }

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -ONE -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = ZERO.toFloat()
                }

                position <= ONE -> { // [-1,1]
                    // Modify the default slide transition to shrink the page as well.
                    val scaleFactor = Math.max(MIN_SCALE, ONE - Math.abs(position))
                    val vertMargin = pageHeight * (ONE - scaleFactor) / TWO
                    val horzMargin = pageWidth * (ONE - scaleFactor) / TWO
                    translationX = if (position < ZERO) {
                        horzMargin - vertMargin / TWO
                    } else {
                        horzMargin + vertMargin / TWO
                    }

                    // Scale the page down (between MIN_SCALE and 1).
                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    // Fade the page relative to its size.
                    alpha = (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (ONE - MIN_SCALE)) * (ONE - MIN_ALPHA)))
                }

                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = ZERO.toFloat()
                }
            }
        }
    }
}