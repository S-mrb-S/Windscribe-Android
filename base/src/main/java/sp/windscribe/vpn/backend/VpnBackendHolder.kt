/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.backend

import de.blinkt.openvpn.VpnProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import sp.windscribe.vpn.apppreference.AppPreferenceHelper
import sp.windscribe.vpn.autoconnection.ProtocolInformation
import sp.windscribe.vpn.backend.ikev2.IKev2VpnBackend
import sp.windscribe.vpn.backend.openvpn.OpenVPNBackend
import sp.windscribe.vpn.backend.wireguard.WireGuardVpnProfile
import sp.windscribe.vpn.backend.wireguard.WireguardBackend
import sp.windscribe.vpn.constants.PreferencesKeyConstants.PROTO_IKev2
import sp.windscribe.vpn.constants.PreferencesKeyConstants.PROTO_STEALTH
import sp.windscribe.vpn.constants.PreferencesKeyConstants.PROTO_TCP
import sp.windscribe.vpn.constants.PreferencesKeyConstants.PROTO_UDP
import sp.windscribe.vpn.constants.PreferencesKeyConstants.PROTO_WIRE_GUARD
import sp.windscribe.vpn.constants.PreferencesKeyConstants.PROTO_WS_TUNNEL
import java.util.UUID
import javax.inject.Singleton

@Singleton
class VpnBackendHolder(
    val scope: CoroutineScope,
    private val preferenceHelper: AppPreferenceHelper,
    private val iKev2VpnBackend: IKev2VpnBackend,
    private val wireguardBackend: WireguardBackend,
    private val openVPNBackend: OpenVPNBackend
) {

    var activeBackend: VpnBackend? = null
    private val vpnLogger = LoggerFactory.getLogger("vpn_backend")

    /**
    @return VpnBackend Based on selected protocol and existing vpn profile.
     */
    private fun getBackend(): VpnBackend? {
        return when (preferenceHelper.selectedProtocol) {
            PROTO_UDP, PROTO_TCP, PROTO_STEALTH, PROTO_WS_TUNNEL -> {
                if (Util.getProfile<VpnProfile>() != null) {
                    return openVPNBackend
                }
                return null
            }

            PROTO_IKev2 -> {
                if (Util.getProfile<org.strongswan.android.data.VpnProfile>() != null) {
                    return iKev2VpnBackend
                }
                return null
            }

            PROTO_WIRE_GUARD -> {
                if (Util.getProfile<WireGuardVpnProfile>() != null) {
                    return wireguardBackend
                }
                return null
            }

            else -> {
                null
            }
        }
    }

    fun connect(protocolInformation: ProtocolInformation, connectionId: UUID) {
        scope.launch {
            val active: Boolean = activeBackend?.active == true
            if (active) {
                vpnLogger.debug("Active VPN backend found.")
                delay(100)
            }
            activeBackend = getBackend()
            activeBackend?.let {
                it.activate()
                it.connect(protocolInformation, connectionId)
            } ?: kotlin.run {
                // Unexpected state.
                delay(500)
                vpnLogger.debug("Could not find vpn backend matching the current vpn profile.")
            }
        }
    }

    suspend fun disconnect(error: VPNState.Error? = null) {
        activeBackend?.disconnect(error) ?: kotlin.run {
            vpnLogger.debug("VPN backend not found.")
        }
    }
}
