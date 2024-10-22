/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.backend.openvpn

import android.content.Intent
import android.net.VpnService
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.VpnStatus.StateListener
import org.slf4j.LoggerFactory
import sp.windscribe.vpn.ServiceInteractor
import sp.windscribe.vpn.Windscribe
import sp.windscribe.vpn.backend.VPNState.Status.Connecting
import sp.windscribe.vpn.backend.utils.WindNotificationBuilder
import sp.windscribe.vpn.backend.utils.WindVpnController
import sp.windscribe.vpn.constants.NotificationConstants
import sp.windscribe.vpn.state.ShortcutStateManager
import sp.windscribe.vpn.state.VPNConnectionStateManager
import javax.inject.Inject

class OpenVPNWrapperService : OpenVPNService(), StateListener {

    @Inject
    lateinit var windNotificationBuilder: WindNotificationBuilder

    @Inject
    lateinit var serviceInteractor: ServiceInteractor

    @Inject
    lateinit var vpnController: WindVpnController

    @Inject
    lateinit var openVPNBackend: OpenVPNBackend

    @Inject
    lateinit var vpnConnectionStateManager: VPNConnectionStateManager

    @Inject
    lateinit var shortcutStateManager: ShortcutStateManager

    private var logger = LoggerFactory.getLogger("vpn_backend")

    override fun onCreate() {
        Windscribe.appContext.serviceComponent.inject(this)
        super.onCreate()
        openVPNBackend.service = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null || intent.action == VpnService.SERVICE_INTERFACE) {
            logger.debug("System relaunched service, starting shortcut state manager")
            shortcutStateManager.connect()
            stopSelf()
            return START_NOT_STICKY
        }
        startForeground(
                NotificationConstants.SERVICE_NOTIFICATION_ID,
                windNotificationBuilder.buildNotification(Connecting)
        )
        return super.onStartCommand(intent, flags, startId)
    }

//    override fun getProfile(): VpnProfile? {
//        return Util.getProfile<VpnProfile>()
//    }
//
//    override fun onProcessRestore(): Boolean {
//        return serviceInteractor.preferenceHelper.globalUserConnectionPreference
//    }

    override fun onDestroy() {
        openVPNBackend.service = null
        windNotificationBuilder.cancelNotification(NotificationConstants.SERVICE_NOTIFICATION_ID)
        super.onDestroy()
    }

    override fun protect(socket: Int): Boolean {
        return super.protect(socket)
    }
}
