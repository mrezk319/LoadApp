package com.udacity

import android.animation.AnimatorInflater
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val myTheme =
        context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0)
    private val paintText = Paint().apply {
        color = myTheme.getColor(R.styleable.LoadingButton_TextColor, 0)
        textSize = 60.0f
        textAlign = Paint.Align.CENTER
    }
    private val paintCircle = Paint().apply { color = Color.RED }
    private val paintRec = Paint().apply { color = Color.DKGRAY }
    private val Circle_PERCENTAGE_VALUE_HOLDER = "CirclePercentage"
    private val Rec_PERCENTAGE_VALUE_HOLDER = "RecPercentage"
    private var circleState = 0f
    private var recState = 0f
    private var clicked = false
    private val background = myTheme.getColor(R.styleable.LoadingButton_backGround, 0)
    private val textClicked = myTheme.getString(R.styleable.LoadingButton_textClicked)
    private val textNotClicked = myTheme.getString(R.styleable.LoadingButton_textNotClicked)
    private var text = textNotClicked
    private fun animateProgress() {
        isClickable = false; clicked = true; text = textClicked
        val circleValue = PropertyValuesHolder.ofFloat(
            Circle_PERCENTAGE_VALUE_HOLDER,
            0f,
            360f
        )
        val recValue = PropertyValuesHolder.ofFloat(
            Rec_PERCENTAGE_VALUE_HOLDER,
            0f,
            width.toFloat()
        )
        val animator = ValueAnimator().apply {
            setValues(circleValue, recValue)
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                circleState = it.getAnimatedValue(Circle_PERCENTAGE_VALUE_HOLDER) as Float
                recState = it.getAnimatedValue(Rec_PERCENTAGE_VALUE_HOLDER) as Float
                invalidate()
            }
        }
        animator.start()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(background)
        if (circleState == 360f) {
            clicked = false
            text = textNotClicked
            isClickable = true
        }
        if (clicked) {
            canvas.drawRect(
                0f,
                0f,
                recState,//////
                height.toFloat(),
                paintRec
            )
            val Xtc = ((width / 2) - ((paintText.descent() + paintText.ascent())) / 2) + 65 * 4
            val Ytc = (height / 2).toFloat()
            canvas.drawArc(
                Xtc + 10 - 30, Ytc - 30,
                Xtc + 40, Ytc + 30,
                0F, circleState,
                true, paintCircle
            )
        }
        canvas.drawText(
            text!!,
            ((width / 2) - ((paintText.descent() + paintText.ascent())) / 2),
            ((height / 2) - ((paintText.descent() + paintText.ascent())) / 2),
            paintText
        )
    }
    override fun performClick(): Boolean {
        animateProgress()
        return super.performClick()
    }


}