package com.example.poc_gc_android_feed

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class VerticalViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    init {
        setPageTransformer(true, VerticalPageTransformer())
        overScrollMode = OVER_SCROLL_NEVER
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercepted = super.onInterceptTouchEvent(swapTouchEvent(ev))
        swapTouchEvent(ev)
        return intercepted
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return super.onTouchEvent(swapTouchEvent(ev))
    }

    private fun swapTouchEvent(event: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()

        val swappedX = event.y / height * width
        val swappedY = event.x / width * height

        event.setLocation(swappedX, swappedY)

        return event
    }

    private inner class VerticalPageTransformer : PageTransformer {
        override fun transformPage(view: android.view.View, position: Float) {
            when {
                position < -1 -> view.alpha = 0f
                position <= 1 -> {
                    view.alpha = 1f
                    view.translationX = view.width * -position
                    val yPosition = position * view.height
                    view.translationY = yPosition
                }
                else -> view.alpha = 0f
            }
        }
    }
}
