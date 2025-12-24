package com.unitywale.oxgtech

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAdsShowOptions
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize

class MainActivity : AppCompatActivity(), IUnityAdsInitializationListener {

    private val unityGameID = "6010399"
    private val testMode = true
    private val adUnitIdInterstitial = "Interstitial_Android"
    private val adUnitIdRewarded = "Rewarded_Android"
    private val adUnitIdBanner = "Banner_Android"

    private lateinit var statusText: TextView
    private lateinit var bannerContainer: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.textViewStatus)
        bannerContainer = findViewById(R.id.bannerFooterContainer)

        // Initialize Buttons
        findViewById<Button>(R.id.btnInterstitial).setOnClickListener {
            showInterstitialAd()
        }

        findViewById<Button>(R.id.btnRewarded).setOnClickListener {
            showRewardedAd()
        }

        // Initialize Unity Ads
        UnityAds.initialize(applicationContext, unityGameID, testMode, this)
    }

    // --- Initialization Listener Methods ---
    override fun onInitializationComplete() {
        val msg = "Initialization Complete"
        Log.d("UnityAdsExample", msg)
        runOnUiThread {
            statusText.text = "Status: Initialized"
            // Load ads once initialized
            loadInterstitial()
            loadRewarded()
            loadBanner(bannerContainer, adUnitIdBanner)
        }
    }

    override fun onInitializationFailed(error: UnityAds.UnityAdsInitializationError?, message: String?) {
        val msg = "Initialization Failed: $error - $message"
        Log.e("UnityAdsExample", msg)
        runOnUiThread {
            statusText.text = msg
        }
    }

    // --- Ad Loading (Interstitial & Rewarded) ---
    private fun loadInterstitial() {
        UnityAds.load(adUnitIdInterstitial, object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placementId: String) {
                Log.d("UnityAdsExample", "Interstitial Loaded: $placementId")
            }

            override fun onUnityAdsFailedToLoad(placementId: String, error: UnityAds.UnityAdsLoadError, message: String) {
                Log.e("UnityAdsExample", "Interstitial Failed to Load: $message")
            }
        })
    }

    private fun loadRewarded() {
        UnityAds.load(adUnitIdRewarded, object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placementId: String) {
                Log.d("UnityAdsExample", "Rewarded Loaded: $placementId")
            }

            override fun onUnityAdsFailedToLoad(placementId: String, error: UnityAds.UnityAdsLoadError, message: String) {
                Log.e("UnityAdsExample", "Rewarded Failed to Load: $message")
            }
        })
    }

    // --- Ad Showing (Interstitial & Rewarded) ---
    private fun showInterstitialAd() {
        UnityAds.show(this, adUnitIdInterstitial, UnityAdsShowOptions(), object : IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(placementId: String, error: UnityAds.UnityAdsShowError, message: String) {
                Log.e("UnityAdsExample", "Interstitial Show Failure: $message")
                Toast.makeText(this@MainActivity, "Failed to show Interstitial", Toast.LENGTH_SHORT).show()
                // Try reloading
                loadInterstitial()
            }

            override fun onUnityAdsShowStart(placementId: String) {
                Log.d("UnityAdsExample", "Interstitial Show Start")
            }

            override fun onUnityAdsShowClick(placementId: String) {
                Log.d("UnityAdsExample", "Interstitial Clicked")
            }

            override fun onUnityAdsShowComplete(placementId: String, state: UnityAds.UnityAdsShowCompletionState) {
                Log.d("UnityAdsExample", "Interstitial Complete")
                // Reload for next time
                loadInterstitial()
            }
        })
    }

    private fun showRewardedAd() {
        UnityAds.show(this, adUnitIdRewarded, UnityAdsShowOptions(), object : IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(placementId: String, error: UnityAds.UnityAdsShowError, message: String) {
                Log.e("UnityAdsExample", "Rewarded Show Failure: $message")
                Toast.makeText(this@MainActivity, "Failed to show Rewarded", Toast.LENGTH_SHORT).show()
                loadRewarded()
            }

            override fun onUnityAdsShowStart(placementId: String) {
                Log.d("UnityAdsExample", "Rewarded Show Start")
            }

            override fun onUnityAdsShowClick(placementId: String) {
                Log.d("UnityAdsExample", "Rewarded Clicked")
            }

            override fun onUnityAdsShowComplete(placementId: String, state: UnityAds.UnityAdsShowCompletionState) {
                Log.d("UnityAdsExample", "Rewarded Complete")
                if (state == UnityAds.UnityAdsShowCompletionState.COMPLETED) {
                    Toast.makeText(this@MainActivity, "Reward Earned!", Toast.LENGTH_SHORT).show()
                }
                loadRewarded()
            }
        })
    }

    // --- Banner Ad ---
    private fun loadBanner(container: RelativeLayout, placementId: String) {
        val bannerView = BannerView(this, placementId, UnityBannerSize(320, 50))
        bannerView.listener = object : BannerView.IListener {
            override fun onBannerLoaded(bannerAdView: BannerView) {
                Log.d("UnityAdsExample", "Banner Loaded")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Banner Loaded", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onBannerFailedToLoad(bannerAdView: BannerView, errorInfo: BannerErrorInfo) {
                Log.e("UnityAdsExample", "Banner Failed to Load: ${errorInfo.errorMessage}")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Banner Error: ${errorInfo.errorMessage}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onBannerClick(bannerAdView: BannerView) {
                Log.d("UnityAdsExample", "Banner Clicked")
            }

            override fun onBannerLeftApplication(bannerAdView: BannerView) {
                Log.d("UnityAdsExample", "Banner Left App")
            }

            override fun onBannerShown(bannerAdView: BannerView?) {
                Log.d("UnityAdsExample", "Banner Shown")
            }
        }
        
        // Add to view hierarchy first
        container.addView(bannerView)
        // Then load
        bannerView.load()
    }
}
