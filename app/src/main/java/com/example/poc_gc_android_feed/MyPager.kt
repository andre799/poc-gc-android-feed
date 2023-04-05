package com.example.poc_gc_android_feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.marginEnd
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MyPagerFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: PagerAdapter
    private var currentPosition = 0
    private var currentPage = 0
    private var loadingMoreVideos = false

    private val postsRepository = PostsRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_pager, container, false)
        val mainView = inflater.inflate(R.layout.fragment_my_pager, container, false)

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
        pagerAdapter = PagerAdapter(this)

        viewPager.adapter = pagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                currentPosition = position

                if (!loadingMoreVideos && positionOffset == 0.0.toFloat() && (currentPosition == pagerAdapter.videos.size - 4 || currentPosition == pagerAdapter.videos.size - 2)) {
                    currentPage++
                    loadVideos()
                }
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadVideos()
    }

    private val scope = CoroutineScope(Dispatchers.Main)

    private fun loadVideos() {
        loadingMoreVideos = true
        scope.launch(Dispatchers.IO) {
            val posts = postsRepository.getVideos(currentPage)
            val videoItems = posts.mapNotNull { it.getSource() }
            withContext(Dispatchers.Main) {
                pagerAdapter.addVideos(videoItems)
                loadingMoreVideos = false
            }
        }
    }
}