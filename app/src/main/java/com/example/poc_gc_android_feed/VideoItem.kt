package com.example.poc_gc_android_feed

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import java.util.concurrent.Executors


class VideoItem : Fragment() {
    var player: ExoPlayer? = null
    private lateinit var playerView: StyledPlayerView
    private var shouldShowAds = false

    private lateinit var adView: NativeAdView
    private lateinit var nativeAd: NativeAd

    companion object {
        fun newInstance(videoUrl: String, shouldShowAds: Boolean): VideoItem {
            val args = Bundle()
            args.putString("videoUrl", videoUrl)
            args.putBoolean("shouldShowAds", shouldShowAds)
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

        shouldShowAds = arguments?.getBoolean("shouldShowAds") ?: false

        if (shouldShowAds) {
            playerView.visibility = View.INVISIBLE

            val builder = AdLoader.Builder(requireContext(), "ca-app-pub-3940256099942544/2247696110")
            builder.forNativeAd { ad ->
                nativeAd = ad
                adView = view.findViewById(R.id.native_ad_layout)

                //Init Native Ads Vies
                val adView: NativeAdView = view.findViewById(R.id.native_ad_layout)
                val adMedia: MediaView = view.findViewById(R.id.adMedia)
                val adHeadline: TextView = view.findViewById(R.id.adHeadline)
                val adBody: TextView = view.findViewById(R.id.adBody)
                val adBtnAction: Button = view.findViewById(R.id.adBtnAction)
                val adAppIcon: ImageView = view.findViewById(R.id.adAppIcon)
                val adPrice: TextView = view.findViewById(R.id.adPrice)
                val adStars: RatingBar = view.findViewById(R.id.adStars)
                val adStore: TextView = view.findViewById(R.id.adStore)
                val adAdvertiser: TextView = view.findViewById(R.id.adAdvertiser)
                //Assign position of views inside the native ad view
                adView.mediaView = adMedia
                adView.headlineView = adHeadline
                adView.bodyView = adBody
                adView.callToActionView = adBtnAction
                adView.iconView = adAppIcon
                adView.priceView = adPrice
                adView.starRatingView = adStars
                adView.storeView = adStore
                adView.advertiserView = adAdvertiser

                //Assign Values to View
                adView.mediaView?.mediaContent = ad.mediaContent!!
                adView.mediaView?.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                (adView.headlineView as TextView).text = ad.headline
                (adView.bodyView as TextView).text = ad.body
                (adView.callToActionView as Button).text = ad.callToAction
                (adView.iconView as ImageView).setImageDrawable(ad.icon?.drawable)
                (adView.priceView as TextView).text = ad.price
                (adView.storeView as TextView).text = ad.store
                (adView.starRatingView as RatingBar).rating = ad.starRating!!.toFloat()
                (adView.advertiserView as TextView).text = ad.advertiser

                adView.setNativeAd(ad)
                adView.visibility = View.VISIBLE
            }

            val adLoader = builder.build()
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                adLoader.loadAd(AdRequest.Builder().build())
            }
            executor.shutdown()

        } else {
            playerView.visibility = View.VISIBLE
            initPlayer(videoUrl!!)
        }
    }

    fun play() {
        player?.play()
    }

    fun pause() {
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        if (!shouldShowAds) play()
    }

    override fun onPause() {
        super.onPause()
        player?.seekTo(0)
        pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
        if(::nativeAd.isInitialized) nativeAd?.destroy()
    }

    private fun releasePlayer() {
        if (player != null) {
            player?.release()
            player = null
        }
    }

    private fun initPlayer(videoURL: String) {
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