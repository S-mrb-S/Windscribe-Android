package sp.windscribe.vpn.sp

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Ads(private val activity: Activity) {
    private var rewardedAd: RewardedAd? = null
    private final var TAG = "Ads"
    private var busy = false
    val backgroundScope = CoroutineScope(Dispatchers.IO)

    fun create() {
        try {
            busy = true
            backgroundScope.launch {
                // Initialize the Google Mobile Ads SDK on a background thread.
                MobileAds.initialize(activity) {}

                delay(500)
                activity.runOnUiThread {
                    val adRequest = AdRequest.Builder().build()
                    RewardedAd.load(
                        activity,
                        "ca-app-pub-9597526652962835~9570533299",
                        adRequest,
                        object : RewardedAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                Log.d(TAG, adError.toString())
                                rewardedAd = null
                            }

                            override fun onAdLoaded(ad: RewardedAd) {
                                Log.d(TAG, "Ad was loaded.")
                                rewardedAd = ad

                                this@launch.launch {
                                    delay(500)
                                    setFullScreen()
                                    delay(100)
                                    busy = false
                                }
                            }
                        })
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setFullScreen() {
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                rewardedAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                rewardedAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }

    }

    fun showTheAd() {
        if(busy){
            rewardedAd?.let { ad ->
                ad.show(activity, OnUserEarnedRewardListener { rewardItem ->
                    // Handle the reward.
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    Log.d(TAG, "User earned the reward. --> $rewardType, $rewardAmount")
                })
            } ?: run {
                Log.d(TAG, "The rewarded ad wasn't ready yet.")
            }
        }else{
            backgroundScope.launch {
                delay(500)
                showTheAd()
            }
        }

    }

}