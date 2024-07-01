package com.natiqhaciyef.uikit.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.natiqhaciyef.common.constants.FIVE
import com.natiqhaciyef.common.constants.ZERO

class CustomCanvasView : View {
    private var pathList = ArrayList<Path>()
    private var paint = Paint()
    private var path = Path()

    constructor(context: Context) : this(context, null, ZERO) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet? = null) : this(context, attributeSet, ZERO) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
            super(context, attributeSet, defStyle)
    {
        init()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(event.x, event.y)
                pathList.add(path)
                invalidate() // This triggers a redraw of the view
                return true
            }

            else -> return false
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (path in pathList) {
            canvas.drawPath(path, paint)
            invalidate()
        }
    }


    private fun init() {
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = FIVE.toFloat()
    }
}