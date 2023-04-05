package com.example.poc_gc_android_feed

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var adView: AdView
    private lateinit var scheduledExecutor: ScheduledExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this)

        adView = findViewById(R.id.adView)
        adView.loadAd(AdRequest.Builder().build())

        scheduledExecutor = Executors.newSingleThreadScheduledExecutor()
        scheduledExecutor.scheduleAtFixedRate(
            {
                runOnUiThread {
                    adView.loadAd(AdRequest.Builder().build())
                }
            },
            30, // delay inicial em segundos
            30, // intervalo entre execuções em segundos
            TimeUnit.SECONDS
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        scheduledExecutor.shutdown()
    }
}