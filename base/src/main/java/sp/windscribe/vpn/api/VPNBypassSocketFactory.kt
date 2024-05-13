package sp.windscribe.vpn.api

import org.slf4j.LoggerFactory
import sp.windscribe.vpn.Windscribe
import sp.windscribe.vpn.backend.wireguard.WireguardBackend
import sp.windscribe.vpn.exceptions.WindScribeException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.Socket
import javax.net.SocketFactory

class VPNBypassSocketFactory : SocketFactory() {
    private var logger = LoggerFactory.getLogger("socket_factory")
    override fun createSocket(): Socket {
        val socket = Socket(Proxy.NO_PROXY)
        try {
            socket.bind(InetSocketAddress(0))
            val activeBackend = Windscribe.appContext.vpnController.vpnBackendHolder.activeBackend
            if (activeBackend is WireguardBackend && activeBackend.active) {
                val protected = activeBackend.service?.protect(socket)
                if (protected == false) {
                    logger.debug("Failed to protect socket from wg tunnel")
                }
            }
        } catch (e: Exception) {
            logger.debug("Error creating socket: ${e.message}")
        }
        return socket
    }

    override fun createSocket(host: String?, port: Int): Socket {
        throw WindScribeException("Not supported")
    }

    override fun createSocket(
        host: String?,
        port: Int,
        localHost: InetAddress?,
        localPort: Int
    ): Socket {
        throw WindScribeException("Not supported")
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket {
        throw WindScribeException("Not supported")
    }

    override fun createSocket(
        address: InetAddress?,
        port: Int,
        localAddress: InetAddress?,
        localPort: Int
    ): Socket {
        throw WindScribeException("Not supported")
    }
}