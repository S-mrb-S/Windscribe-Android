package sp.windscribe.vpn.services.firebasecloud

import android.content.pm.PackageManager
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import sp.windscribe.vpn.BuildConfig
import sp.windscribe.vpn.Windscribe
import sp.windscribe.vpn.constants.NetworkKeyConstants
import sp.windscribe.vpn.services.FirebaseManager
import org.slf4j.LoggerFactory

class FireBaseManagerImpl(private val context: Windscribe): FirebaseManager {
    private val logger = LoggerFactory.getLogger("firebase_m")
    override fun getFirebaseToken(callback: (MutableMap<String, String>) -> Unit) {
        val sessionMap: MutableMap<String, String> = java.util.HashMap()
        if (BuildConfig.API_KEY.isEmpty()) {
            callback(sessionMap)
        } else {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    logger.debug("Failed to get token.")
                } else {
                    val newToken = task.result
                    sessionMap[NetworkKeyConstants.FIREBASE_DEVICE_ID_KEY] = newToken
                }
                callback(sessionMap)
            }
        }
    }

    override fun initialise() {
        FirebaseApp.initializeApp(context, FirebaseOptions.Builder()
                .setGcmSenderId(BuildConfig.GCM_SENDER_ID)
                .setApplicationId(BuildConfig.APP_ID)
                .setProjectId(BuildConfig.PROJECT_ID)
                .setApiKey(BuildConfig.API_KEY)
                .build())
    }

    override val isPlayStoreInstalled: Boolean
        get() = try {
            Windscribe.appContext.packageManager
                    .getPackageInfo(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
}