package sp.windscribe.vpn.model

import sp.windscribe.vpn.autoconnection.ProtocolConnectionStatus
import sp.windscribe.vpn.autoconnection.ProtocolInformation
import sp.windscribe.vpn.constants.PreferencesKeyConstants

data class OpenVPNConnectionInfo(
    val serverConfig: String,
    val ip: String,
    val port: String,
    val protocol: String,
    val username: String,
    val password: String
) {
    val base64EncodedServerConfig =
        String(sp.windscribe.vpn.encoding.encoders.Base64.encode(serverConfig.toByteArray()))

    fun getProtocolInformation(): ProtocolInformation {
        var protocol = PreferencesKeyConstants.PROTO_TCP
        if (protocol == "udp") {
            protocol = PreferencesKeyConstants.PROTO_UDP
        }
        return ProtocolInformation(
            protocol, port, "", type = ProtocolConnectionStatus.NextUp
        )
    }

    override fun toString(): String {
        return "$ip:$port $protocol"
    }
}