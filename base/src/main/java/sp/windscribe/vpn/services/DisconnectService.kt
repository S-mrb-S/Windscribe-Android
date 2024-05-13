/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.services

import android.app.IntentService
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import sp.windscribe.vpn.ServiceInteractor
import sp.windscribe.vpn.Windscribe
import sp.windscribe.vpn.backend.utils.WindVpnController
import sp.windscribe.vpn.state.VPNConnectionStateManager
import javax.inject.Inject

class DisconnectService : IntentService("DisconnectService") {

    @Inject
    lateinit var controller: WindVpnController

    @Inject
    lateinit var disconnectServiceInteractor: ServiceInteractor

    @Inject
    lateinit var vpnConnectionStateManager: VPNConnectionStateManager

    @Inject
    lateinit var scope: CoroutineScope

    private val logger = LoggerFactory.getLogger("disconnect_service")

    override fun onCreate() {
        super.onCreate()
        Windscribe.appContext.serviceComponent.inject(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            scope.launch {
                logger.info("Stopping vpn services from notification.")
                disconnectServiceInteractor.preferenceHelper.globalUserConnectionPreference = false
                controller.disconnectAsync()
            }
        }
    }
}
