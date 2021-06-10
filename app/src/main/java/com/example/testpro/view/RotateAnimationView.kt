package com.example.testpro.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.example.testpro.R

class RotateAnimationView : View {

    private val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
        duration = 5000
        fillAfter = true
        repeatCount = Animation.INFINITE
        interpolator = LinearInterpolator()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    init {
        animation = rotateAnimation
    }

    fun bind(mode: MatchingFloatView.Mode) {
        if (mode == MatchingFloatView.Mode.NORMAL) {
            setBackgroundResource(R.drawable.ic_searching_matching_float)
        } else {
            setBackgroundResource(R.drawable.ic_matching_matching_float)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animation = rotateAnimation
        animation.startNow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animation.cancel()
    }
}