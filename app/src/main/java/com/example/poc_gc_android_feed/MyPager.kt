package com.example.poc_gc_android_feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.viewpager2.widget.ViewPager2

class MyPagerFragment : Fragment() {

    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_pager, container, false)

        viewPager = view.findViewById(R.id.vertical_view_pager)
        viewPager.setPageTransformer(FadePageTransformer())
        viewPager.offscreenPageLimit = 3

        val interpolator = LinearOutSlowInInterpolator()
        val scrollDuration = 250.toLong()

        viewPager.apply {
            // Reduz a velocidade da animação para torná-la mais linear
            val scroller = ViewPagerScroller(context, interpolator, scrollDuration)
            scroller.initViewPagerScroll(this)
        }

        // Criando uma instância do VideoPagerAdapter e passando a lista de vídeos para ele
        val videoPagerAdapter = PagerAdapter(this, listOf(
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
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