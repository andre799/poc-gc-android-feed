package com.example.poc_gc_android_feed

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class ViewPagerScroller(context: Context, inter: Interpolator, duration: Long) : Scroller(context, inter) {
    private var scrollDuration = duration
    private var interpolator = inter

    fun initViewPagerScroll(viewPager: ViewPager2) {
        try {
            val viewPagerView = viewPager.getChildAt(0) as RecyclerView
            val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
            recyclerViewField.isAccessible = true
            recyclerViewField.set(viewPager, viewPagerView)

            val interpolatorField = ViewPager2::class.java.getDeclaredField("sInterpolator")
            interpolatorField.isAccessible = true
            interpolatorField.set(viewPager, interpolator)

            val scrollerField = ViewPager2::class.java.getDeclaredField("mScroller")
            scrollerField.isAccessible = true
            scrollerField.set(viewPager, this)
        } catch (e: NoSuchFieldException) {
            // Ignora a exceção
        } catch (e: IllegalAccessException) {
            // Ignora a exceção
        }
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, scrollDuration.toInt())
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, scrollDuration.toInt())
    }

    fun setScrollDuration(duration: Long) {
        scrollDuration = duration
    }
}