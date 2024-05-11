/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.backend

import sp.windscribe.vpn.autoconnection.ProtocolInformation
import java.util.UUID

class VPNState(
    val status: Status,
    var error: Error? = null,
    val ip: String? = null,
    var protocolInformation: ProtocolInformation? = null,
    var connectionId: UUID? = null
) {
    enum class Status {
        Connecting, Connected, Disconnected, Disconnecting, RequiresUserInput, ProtocolSwitch, UnsecuredNetwork, InvalidSession
    }

    enum class ErrorType {
        UserReconnect, UserDisconnect, AuthenticationError, WireguardAuthenticationError, GenericError, TimeoutError, WireguardApiError, ConnectivityTestFailed
    }

    data class Error(
        val error: ErrorType,
        val message: String = "Unknown",
        val showError: Boolean = false
    )
}
