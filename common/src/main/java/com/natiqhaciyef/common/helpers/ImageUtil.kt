package com.natiqhaciyef.common.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.ZERO

fun ImageView.loadImage(
    uri: String,
    context: Context = this.context,
) {
    val animationPlaceholder = LottiePlaceholderDrawable(
        context,
        R.raw.loading_animation,
        R.drawable.non
    )

    val requestOption = RequestOptions.placeholderOf(animationPlaceholder).error(R.drawable.non)

    Glide.with(context)
        .setDefaultRequestOptions(requestOption)
        .load(uri)
        .into(this)

    animationPlaceholder.setAnimationProgress(ZERO.toFloat())
    animationPlaceholder.invalidateSelf()

}

fun ImageView.clearImage(
    context: Context = this.context,
) {
    Glide.with(context)
        .load(EMPTY_STRING)
        .into(this)
}

class LottiePlaceholderDrawable(context: Context, animationResId: Int, staticResId: Int) :
    Drawable() {

    private val animationView: LottieAnimationView = LottieAnimationView(context)
    private val staticDrawable: Drawable = ContextCompat.getDrawable(context, staticResId)!!

    init {
        animationView.setAnimation(animationResId)
        animationView.playAnimation()
        animationView.visibility = View.VISIBLE
    }

    override fun draw(canvas: Canvas) {
        if (!animationView.isAnimating) {
            staticDrawable.draw(canvas)
            return
        }
        animationView.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        animationView.alpha = alpha.toFloat()
        staticDrawable.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        animationView.colorFilter = colorFilter
        staticDrawable.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    fun setAnimationProgress(progress: Float) {
        animationView.progress = progress
    }
}

