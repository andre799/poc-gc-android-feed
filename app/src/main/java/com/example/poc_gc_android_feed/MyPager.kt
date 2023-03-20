package com.example.poc_gc_android_feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.viewpager2.widget.ViewPager2

class MyPagerFragment : Fragment() {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_pager, container, false)

        viewPager = view.findViewById(R.id.vertical_view_pager)
        viewPager.setPageTransformer(FadePageTransformer())
        viewPager.offscreenPageLimit = 3
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        val interpolator = LinearOutSlowInInterpolator()
        val scrollDuration = 250.toLong()

        viewPager.apply {
            // Reduz a velocidade da animação para torná-la mais linear
            val scroller = ViewPagerScroller(context, interpolator, scrollDuration)
            scroller.initViewPagerScroll(this)
        }

        // Criando uma instância do VideoPagerAdapter e passando a lista de vídeos para ele
        val videoPagerAdapter = PagerAdapter(this, listOf(
            "https://assets.mixkit.co/videos/preview/mixkit-winter-fashion-cold-looking-woman-concept-video-39874-large.mp4",
            "https://assets.mixkit.co/videos/preview/mixkit-tree-with-yellow-flowers-1173-large.mp4",
            "https://assets.mixkit.co/videos/preview/mixkit-mother-with-her-little-daughter-eating-a-marshmallow-in-nature-39764-large.mp4",
            "https://assets.mixkit.co/videos/preview/mixkit-man-runs-past-ground-level-shot-32809-large.mp4",
            "https://assets.mixkit.co/videos/preview/mixkit-photoshoot-of-a-girl-posing-in-the-desert-34409-large.mp4",
            "https://assets.mixkit.co/videos/preview/mixkit-young-man-skating-in-a-parking-lot-34550-large.mp4",
        ))

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            private var currentPosition = 0

            override fun onPageSelected(position: Int) {
                if (position != currentPosition) {
                    val currentFragment = videoPagerAdapter.getFragmentAtPosition(currentPosition)
                    currentFragment?.pause()

                    val nextFragment = videoPagerAdapter.getFragmentAtPosition(position)
                    nextFragment?.play()

                    val allFragments = videoPagerAdapter.getAllFragments()

                    for (fragment in allFragments) {
                        if(fragment != nextFragment) fragment?.pause()
                    }
                }

                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })

        // Definindo o adapter do ViewPager como sendo a instância do VideoPagerAdapter criada anteriormente
        viewPager.adapter = videoPagerAdapter

        return view
    }
}