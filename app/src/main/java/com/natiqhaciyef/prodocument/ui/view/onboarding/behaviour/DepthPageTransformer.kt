package com.natiqhaciyef.prodocument.ui.view.onboarding.behaviour

import android.view.View
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.ZERO

@RequiresApi(21)
class DepthPageTransformer : ViewPager2.PageTransformer {
    companion object {
        private const val MIN_SCALE = 0.75f
    }

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            when {
                position < -ONE -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = ZERO.toFloat()
                }

                position <= ZERO -> { // [-1,0]
                    // Use the default slide transition when moving to the left page.
                    alpha = ONE.toFloat()
                    translationX = ZERO.toFloat()
                    translationZ = ZERO.toFloat()
                    scaleX = ONE.toFloat()
                    scaleY = ONE.toFloat()
                }

                position <= ONE -> { // (0,1]
                    // Fade the page out.
                    alpha = ONE - position

                    // Counteract the default slide transition.
                    translationX = pageWidth * -position
                    // Move it behind the left page.
                    translationZ = -ONE.toFloat()

                    // Scale the page down (between MIN_SCALE and 1).
                    val scaleFactor = (MIN_SCALE + (ONE - MIN_SCALE) * (ONE - Math.abs(position)))
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }

                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = ZERO.toFloat()
                }
            }
        }
    }
}