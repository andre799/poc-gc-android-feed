package com.example.poc_gc_android_feed

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource


class VideoItem : Fragment(){
    var player: ExoPlayer? = null
    private lateinit var playerView: StyledPlayerView
    private var playbackPosition: Long = 0

    companion object {
        fun newInstance(videoUrl: String, isFirstVideo: Boolean): VideoItem {
            val args = Bundle()
            args.putString("videoUrl", videoUrl)
            args.putBoolean("isFirstVideo", isFirstVideo)
            val fragment = VideoItem()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.video_item, container, false)

        // Get PlayerView by its id
        playerView = view.findViewById(R.id.player_view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoUrl = arguments?.getString("videoUrl")
        val isFirstVideo = arguments?.getBoolean("isFirstVideo")

        initPlayer(videoUrl!!, isFirstVideo ?: false)
    }

    fun play(){
        player?.play()
    }

    fun pause(){
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        player?.seekTo(playbackPosition)
        player?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        playbackPosition = player?.currentPosition ?: 0
        player?.playWhenReady = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
    }

    private fun releasePlayer() {
        if (player != null) {
            player?.release()
            player = null
        }
    }

    private fun initPlayer(videoURL: String, isFirstVideo: Boolean) {
        // Create a player instance.
        player = ExoPlayer.Builder(requireContext()).build()

        // Bind the player to the view.
        playerView.player = player

        // Setting exoplayer when it is ready.
        player!!.repeatMode = Player.REPEAT_MODE_ALL

        // Set the media source to be played.
        player!!.setMediaSource(buildMediaSource(videoURL))

        // Prepare the player.
        player!!.prepare()
    }

    // Creating mediaSource
    private fun buildMediaSource(videoURL: String): MediaSource {
        // Create a data source factory.
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

        // Create a progressive media source pointing to a stream uri.

        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoURL))
    }
}