package com.example.poc_gc_android_feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginEnd
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.viewpager2.widget.ViewPager2
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MyPagerFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private var currentPosition = 0

    private val videoApiClient = PostsRepository()
    private var isLoading = false

    private var _videos = mutableListOf<VideoItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_pager, container, false)

        viewPager = view.findViewById(R.id.vertical_view_pager)
        viewPager.setPageTransformer(FadePageTransformer())
        viewPager.offscreenPageLimit = 6
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
            "https://d2c2zt048tit41.cloudfront.net/post/media/processed/video/2023/03/14/641128c7e8288732872402/641128c80d1e3704105797_standardized.mp4",
            "https://d2c2zt048tit41.cloudfront.net/post/media/processed/video/2023/03/16/6413970c3091c131855780/6413970c4b19e813324051_standardized.mp4",
            "https://d2c2zt048tit41.cloudfront.net/post/media/processed/video/2023/03/14/6410d518529ca639861848/6410d518664af477179873_standardized.mp4",
            "https://d2c2zt048tit41.cloudfront.net/post/media/processed/video/2023/03/15/6411a09537a12742492949/6411a0954b026124729150_standardized.mp4",
            "https://d2c2zt048tit41.cloudfront.net/post/media/processed/video/2023/02/24/63f93b2e671ab307811258/63f93b2e79bb5106613662_standardized.mp4",
            "https://d2c2zt048tit41.cloudfront.net/post/media/processed/video/2023/03/16/6413a53d1a03d964179461/6413a53d31b2c890358329_standardized.mp4",
        ))

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
//                if (position != currentPosition) {
//                    val currentFragment = videoPagerAdapter.getFragmentAtPosition(currentPosition)
//                    currentFragment?.pause()
//
//                    val nextFragment = videoPagerAdapter.getFragmentAtPosition(position)
//                    nextFragment?.play()
//
//                    val allFragments = videoPagerAdapter.getAllFragments()
//
//                    for (fragment in allFragments) {
//                        if(fragment != nextFragment) fragment?.pause()
//                    }
//                }

                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })

        viewPager.adapter = videoPagerAdapter

        return view
    }

}