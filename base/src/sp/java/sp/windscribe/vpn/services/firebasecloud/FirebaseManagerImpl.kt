package sp.windscribe.vpn.services.firebasecloud

import sp.windscribe.vpn.Windscribe
import sp.windscribe.vpn.services.FirebaseManager

class FirebaseManagerImpl(private val context: Windscribe) : FirebaseManager {
    override fun initialise() {

    }

    override fun getFirebaseToken(callback: (MutableMap<String, String>) -> Unit) {
        callback(mutableMapOf())
    }

    override val isPlayStoreInstalled: Boolean
        get() = false
}