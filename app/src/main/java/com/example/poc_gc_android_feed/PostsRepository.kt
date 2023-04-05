package com.example.poc_gc_android_feed

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.Request

class PostsRepository {
    // HTTP Client
    private val client = OkHttpClient()

    fun getVideos(page: Int): List<PostModel> {
        try {
            val url = "https://king-kong-qa.gotchosen.com/api/v1.5/post/trending?page=$page&limit=8"
            val request = Request.Builder()
                .url(url)
                .addHeader("Accept-Language", "pt-BR")
                .addHeader("user-country", "BR")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw Exception("Failed to get videos")
            }

            val responseBody = response.body?.string()
            return parseVideos(responseBody)
        }catch (e: Exception) {
            print(e)
            return mutableListOf<PostModel>()
        }
    }

    private fun parseVideos(responseBody: String?): List<PostModel> {
        val gson = Gson()
        val videosJson = gson.fromJson(responseBody, JsonObject::class.java)
        val videoItems = mutableListOf<PostModel>()

        videosJson.getAsJsonArray("items").forEach { item ->
            val id = item.asJsonObject.get("id").asLong
            val author = gson.fromJson(item.asJsonObject.get("author"), Author::class.java)
            val description = item.asJsonObject.get("description").asString
            val mediasJson = item.asJsonObject.get("medias").asJsonObject
            val medias = mutableMapOf<String, Media>()

            mediasJson.keySet().forEach { mediaType ->
                val mediaJson = mediasJson.getAsJsonObject(mediaType)
                val media = gson.fromJson(mediaJson, Media::class.java)
                media.mediaType = mediaType
                medias[mediaType] = media
            }

            val videoItem = PostModel(id, author, description, medias)
            videoItems.add(videoItem)
        }

        return videoItems
    }
}

data class PostModel(
    val id: Long,
    val author: Author,
    val description: String,
    val medias: Map<String, Media>
){
    fun getSource(): String? {
        medias["high_quality"]?.let {
            return it.source
        }
        return null
    }
}

data class Author(
    val id: Long,
    val username: String,
    val avatarUrl: String
)

data class Media(
    val postId: Long,
    val id: Long,
    val mimeType: String,
    val source: String,
    val original: String,
    var mediaType: String,
    val createdAt: String,
    val updatedAt: String,
    val expirationTimestamp: Long,
    val expiration: String
)