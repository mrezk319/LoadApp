package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var valueOfAnimator = ValueAnimator()
    private var progressCount = 0.0
    private var theWidth = 0
    private var theHight = 0
    private var buttonState: ButtonStates by Delegates.observable<ButtonStates>(ButtonStates.Completed) { p, old, new ->
    }
    private val rectangle = RectF()
    private val textBoundRect = Rect()
    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        progressCount = (it.animatedValue as Float).toDouble()
        invalidate()
        if (progressCount == 100.0) {
            onDownloaded()
        }
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 35.0f
        typeface = Typeface.create("", Typeface.BOLD)
        color = resources.getColor(R.color.colorPrimary)
    }
    init {
        isClickable = true
        valueOfAnimator = AnimatorInflater.loadAnimator(
            context,
            R.animator.loading_animation
        ) as ValueAnimator
        valueOfAnimator.addUpdateListener(updateListener)
    }
    override fun performClick(): Boolean {
        super.performClick()
        if (buttonState == ButtonStates.Completed) buttonState = ButtonStates.Loading
        valueOfAnimator.start()
        return true
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        theWidth = w
        theHight = h
    }
    fun onDownloaded() {
        valueOfAnimator.cancel()
        buttonState = ButtonStates.Completed
        invalidate()
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        theWidth = w
        theHight = h
        setMeasuredDimension(w, h)
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val buttonText =
            if (buttonState == ButtonStates.Loading) resources.getString(R.string.button_loading)
            else resources.getString(R.string.button_download)
        canvas?.drawRect(0.0f, 0.0f, width.toFloat(), height.toFloat(), paint)
        if (buttonState == ButtonStates.Loading) {
            paint.color = resources.getColor(R.color.colorPrimaryDark)
            canvas?.drawRect(
                0f, 0f,
                (theWidth * (progressCount / 100)).toFloat(), height.toFloat(), paint
            )

            paint.getTextBounds(buttonText,0,buttonText.length,textBoundRect)
            val centerX = measuredWidth.toFloat() / 2
            val centerY = measuredHeight.toFloat() / 2
            paint.color = resources.getColor(R.color.colorAccent)
            rectangle.set(centerX+textBoundRect.right/2+40.0f, 30.0f, centerX+textBoundRect.right/2+80.0f, measuredHeight.toFloat() -25.0f )
            canvas?.drawArc(
                rectangle,
                0f, (360 * (progressCount / 100)).toFloat(),
                true,
                paint
            )

        }
        else if (buttonState == ButtonStates.Completed) {
            paint.color = resources.getColor(R.color.colorPrimary)
            canvas?.drawRect(
                0f, 0f,
                (theWidth * (progressCount / 100)).toFloat(), theHight.toFloat(), paint
            )
        }
        paint.color = Color.WHITE
        canvas?.drawText(buttonText, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)
        paint.color = resources.getColor(R.color.colorPrimary)
    }
}