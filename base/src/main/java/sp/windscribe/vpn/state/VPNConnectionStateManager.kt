/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.state

import android.os.Build
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import sp.windscribe.vpn.R
import sp.windscribe.vpn.Windscribe
import sp.windscribe.vpn.apppreference.PreferencesHelper
import sp.windscribe.vpn.autoconnection.AutoConnectionManager
import sp.windscribe.vpn.backend.VPNState
import sp.windscribe.vpn.backend.VPNState.Status.Connected
import sp.windscribe.vpn.backend.VPNState.Status.Disconnected
import sp.windscribe.vpn.commonutils.WindUtilities
import sp.windscribe.vpn.repository.UserRepository
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Singleton

@Singleton
class VPNConnectionStateManager(
        val scope: CoroutineScope,
        val autoConnectionManager: AutoConnectionManager,
        val preferencesHelper: PreferencesHelper,
        val userRepository: Lazy<UserRepository>
) {
    private val logger = LoggerFactory.getLogger("vpn_backend")

    private val _events = MutableStateFlow(VPNState(Disconnected))
    val state: StateFlow<VPNState> = _events

    fun isVPNActive(): Boolean {
        return state.value.status != Disconnected
    }

    fun isVPNConnected(): Boolean {
        return state.value.status == Connected
    }

    fun setState(newState: VPNState, force: Boolean = false) {
        scope.launch {
            if (AppLifeCycleObserver.isInForeground.not() && newState.status == Disconnected) {
                preferencesHelper.isReconnecting = false
            }
            val lastState = "${_events.value.status}${_events.value.connectionId}>"
            val state = "${newState.status}${newState.connectionId}>"
            if (lastState != state || force) {
                _events.emit(newState)
            }
        }
    }

    init {
        val start = AtomicBoolean(false)
        scope.launch {
            state.collectLatest {
                if (start.getAndSet(true)) {
                    logger.debug("VPN state changed to ${it.status}")
                } else {
                    val logFile = Windscribe.appContext.resources.getString(
                            R.string.log_file_header,
                            Build.VERSION.SDK_INT,
                            Build.BRAND,
                            Build.DEVICE,
                            Build.MODEL,
                            Build.MANUFACTURER,
                            Build.VERSION.RELEASE,
                            WindUtilities.getVersionCode()
                    )
                    logger.info(logFile)
                    logger.debug("VPN state initialized with ${it.status}")
                    if (autoConnectionManager.listOfProtocols.isEmpty()) {
                        autoConnectionManager.reset()
                    }
                }
            }
        }
    }
}
