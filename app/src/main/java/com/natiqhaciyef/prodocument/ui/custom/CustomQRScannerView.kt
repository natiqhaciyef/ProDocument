package com.natiqhaciyef.prodocument.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class CustomQRScannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private var width = 0f
    private var height = 0f

    private val cornerRadius = 32f.dpToPx(context) // 32 dp converted to pixels

    private fun getRect() = RectF(
        rectLeft,
        rectTop,
        rectRight,
        rectBottom
    )

    private val rectLeft
        get() = run {
            (width * 0.48f) - (rectSize * 0.5f)
        }

    private val rectTop
        get() = run {
            (height * 0.47f) - (rectSize * 0.5f)
        }

    private val rectRight
        get() = run {
            (width * 0.52f) + (rectSize * 0.5f)
        }

    private val rectBottom
        get() = run {
            (height * 0.55f) + (rectSize * 0.5f)
        }

    private val rectSize
        get() = run {
            val size = if (width < height) {
                width
            } else {
                height
            }
            size * 0.8f
        }

    init {
        paint.apply {
            style = Paint.Style.FILL
            color = 0x80000000.toInt() // Adjust alpha for desired transparency
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(drawPath(), paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Set updated dimensions

        width = w.toFloat()
        height = h.toFloat()
    }


    private fun drawPath(): Path {
        return path.apply {
            reset() // Reset path before drawing
            fillType = Path.FillType.WINDING

            addRect(0f, 0f, width, height, Path.Direction.CW)
            addRoundRect(getRect(), cornerRadius, cornerRadius, Path.Direction.CCW)
        }
    }

    private fun Float.dpToPx(context: Context): Float {
        return this * context.resources.displayMetrics.density
    }
}
