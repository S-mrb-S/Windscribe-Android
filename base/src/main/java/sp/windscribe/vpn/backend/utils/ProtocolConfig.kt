/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.backend.utils

import sp.windscribe.vpn.constants.PreferencesKeyConstants.PROTO_STEALTH
import sp.windscribe.vpn.constants.PreferencesKeyConstants.PROTO_TCP
import sp.windscribe.vpn.constants.PreferencesKeyConstants.PROTO_UDP
import sp.windscribe.vpn.constants.PreferencesKeyConstants.PROTO_WIRE_GUARD
import sp.windscribe.vpn.constants.PreferencesKeyConstants.PROTO_WS_TUNNEL

class ProtocolConfig(var protocol: String, var port: String, val type: Type) {
    enum class Type {
        Preferred, Manual, Auto,
    }

    override fun toString(): String {
        return "Protocol Config: $protocol:$port"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProtocolConfig

        if (protocol != other.protocol) return false
        if (port != other.port) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = protocol.hashCode()
        result = 31 * result + port.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    val heading: String
        get() {
            return when (protocol) {
                PROTO_UDP -> "UDP"
                PROTO_TCP -> "TCP"
                PROTO_STEALTH -> "Stealth"
                PROTO_WS_TUNNEL -> "WStunnel"
                PROTO_WIRE_GUARD -> "WireGuard"
                else -> "IKEv2"
            }
        }
}
