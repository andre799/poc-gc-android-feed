package com.example.poc_gc_android_feed

import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import java.lang.Math.abs

class FadePageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height

        when {
            position < -1 -> {
                // This page is way off-screen to the left.
                page.alpha = 0f
            }
            position <= 1 -> {
                page.alpha = 1f

                // set Y position to swipe in from top
                val yPosition = position * pageHeight
                page.translationY = yPosition
            }
            else -> {
                // This page is way off-screen to the right.
                page.alpha = 0f
            }
        }
    }
}