package com.example.poc_gc_android_feed

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class PagerAdapter(private val fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    val videos: ArrayList<String> = ArrayList()

    fun addVideos(newVideos: List<String?>) {
        val startIndex = videos.size
        videos.addAll(newVideos.filterNotNull())
        notifyItemRangeInserted(startIndex, newVideos.size)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = videos.size

    override fun createFragment(position: Int): Fragment =
        VideoItem.newInstance(videos[position], position % 7 == 0 && position != 0)
}