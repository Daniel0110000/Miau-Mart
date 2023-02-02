package com.daniel.miaumart.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import com.daniel.miaumart.R

class BorderCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var isSelectedV = false
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private var borderNormalColor: Int = 0
    private var borderSelectedColor: Int = 0
    private var borderWith: Float = 0f

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BorderCardView)
        borderNormalColor =
            typedArray.getColor(R.styleable.BorderCardView_borderNormalColor, Color.GRAY)
        borderSelectedColor =
            typedArray.getColor(R.styleable.BorderCardView_borderSelectedColor, Color.GREEN)
        borderWith = typedArray.getDimension(R.styleable.BorderCardView_borderWidth, 2f)
        paint.strokeWidth = borderWith
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val halfStrokeWith = paint.strokeWidth / 2f
        rect.inset(halfStrokeWith, halfStrokeWith)
        paint.color = if (isSelectedV) borderSelectedColor else borderNormalColor
        canvas.drawRoundRect(rect, 10f, 10f, paint)
    }

    override fun setSelected(selected: Boolean) {
        this.isSelectedV = selected
        invalidate()
    }

}