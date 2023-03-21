package com.example.poc_gc_android_feed

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class PostsRepository {
    // HTTP Client
    private val client = OkHttpClient()

    fun getVideos(page: Int): List<VideoItem> {
        val url = "https://king-kong-qa.gotchosen.com/api/v1.5/post/trending?page=$page&limit=10"
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw Exception("Failed to get videos")
        }

        val responseBody = response.body?.string()
        return parseVideos(responseBody)
    }

    private fun parseVideos(responseBody: String?): List<VideoItem> {
        // Parse the JSON response and create a list of Video objects
        // You can use any JSON parsing library of your choice, such as Gson or Jackson
    }
}