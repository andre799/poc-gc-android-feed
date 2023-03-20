package com.example.poc_gc_android_feed

import android.annotation.SuppressLint
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.max

@SuppressLint("ResourceType")
class FadePageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageMargin = page.context.resources.getDimensionPixelSize(R.dimen.fab_margin)
        val offset = position * -(2 * pageMargin + pageWidth)

        if (position < -1) {
            page.translationY = -offset
        } else if (position <= 1) {
            val scaleFactor = max(0.7f, 1 - kotlin.math.abs(position - 0.14285715f))
            page.translationY = offset
            page.scaleY = scaleFactor
            page.alpha = scaleFactor
        } else {
            page.alpha = 0f
            page.translationY = offset
        }
    }
}