package sp.windscribe.vpn.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import sp.windscribe.vpn.api.IApiCallManager
import sp.windscribe.vpn.apppreference.PreferencesHelper
import sp.windscribe.vpn.backend.VPNState
import sp.windscribe.vpn.commonutils.Ext.toResult
import sp.windscribe.vpn.commonutils.WindUtilities
import sp.windscribe.vpn.constants.PreferencesKeyConstants
import sp.windscribe.vpn.state.VPNConnectionStateManager

class IpRepository(
        private val scope: CoroutineScope,
        private val preferenceHelper: PreferencesHelper,
        private val apiCallManagerV2: IApiCallManager,
        private val vpnConnectionStateManager: VPNConnectionStateManager
) {
    private val logger = LoggerFactory.getLogger("ip_repository")
    private val _state = MutableSharedFlow<RepositoryState<String>>()
    val state = _state

    init {
        update()
        scope.launch {
            vpnConnectionStateManager.state.collectLatest {
                if (it.status == VPNState.Status.Connected) {
                    loadIpFromStorage()
                }
                if (it.status == VPNState.Status.Disconnected) {
                    update()
                }
            }
        }
    }

    fun update() {
        scope.launch {
            _state.emit(RepositoryState.Loading())
            if (WindUtilities.isOnline()) {
                apiCallManagerV2.getConnectedIp().toResult().onSuccess {
                    when (val result = it.callResult<String>()) {
                        is CallResult.Error -> loadIpFromStorage()
                        is CallResult.Success -> {
                            val ipAddress = getModifiedIpAddress(result.data.trim())
                            preferenceHelper.saveResponseStringData(
                                    PreferencesKeyConstants.USER_IP, ipAddress
                            )
                            _state.emit(RepositoryState.Success(ipAddress))
                        }
                    }
                }.onFailure {
                    logger.error("Failed to get Ip.", it)
                    loadIpFromStorage()
                }
            } else {
                loadIpFromStorage()
            }
        }
    }

    private suspend fun loadIpFromStorage() {
        preferenceHelper.getResponseString(PreferencesKeyConstants.USER_IP)?.let {
            _state.emit(RepositoryState.Success(it))
        } ?: kotlin.run {
            _state.emit(RepositoryState.Error("No saved ip found."))
        }
    }

    private fun getModifiedIpAddress(ipResponse: String): String {
        var ipAddress: String?
        if (ipResponse.length >= 32) {
            logger.info("Ipv6 address. Truncating and saving ip data...")
            ipAddress = ipResponse.replace("0000".toRegex(), "0")
            ipAddress = ipAddress.replace("000".toRegex(), "")
            ipAddress = ipAddress.replace("00".toRegex(), "")
        } else {
            logger.info("Saving Ipv4 address...")
            ipAddress = ipResponse
        }
        return ipAddress
    }
}