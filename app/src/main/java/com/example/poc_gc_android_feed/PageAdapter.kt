package com.example.poc_gc_android_feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.poc_gc_android_feed.databinding.ActivityMainBinding
import com.example.poc_gc_android_feed.databinding.VideoItemBinding


class PagerAdapter(private val fragment: Fragment, private val videoUrls: List<String>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = videoUrls.size

    override fun createFragment(position: Int): Fragment =
        VideoItem.newInstance(videoUrls[position], position == 0)

    fun getFragmentAtPosition(position: Int): VideoItem? {
        return fragment.childFragmentManager.findFragmentByTag("f$position") as? VideoItem
    }

    fun getAllFragments(): List<VideoItem?> {
        return fragment.childFragmentManager.fragments.map {
            it as? VideoItem
        }
    }
}