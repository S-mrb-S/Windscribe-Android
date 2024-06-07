package sp.windscribe.mobile.welcome.viewmodal

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import sp.windscribe.mobile.dialogs.EmergencyAccountRequestDialog
import sp.windscribe.mobile.dialogs.UsernameAndPasswordRequestDialog
import sp.windscribe.mobile.sp.util.StaticData
import sp.windscribe.mobile.welcome.state.EmergencyConnectUIState
import sp.windscribe.vpn.backend.VPNState.Status.Connected
import sp.windscribe.vpn.backend.VPNState.Status.Connecting
import sp.windscribe.vpn.backend.VPNState.Status.Disconnected
import sp.windscribe.vpn.backend.VPNState.Status.Disconnecting
import sp.windscribe.vpn.backend.VPNState.Status.RequiresUserInput
import sp.windscribe.vpn.backend.utils.WindVpnController
import sp.windscribe.vpn.serverlist.entity.ConfigFile
import sp.windscribe.vpn.state.VPNConnectionStateManager

class EmergencyConnectViewModal(
        private val scope: CoroutineScope,
        private val windVpnController: WindVpnController,
        private val vpnConnectionStateManager: VPNConnectionStateManager
) : ViewModel() {
    private val logger = LoggerFactory.getLogger("e_connect_v")
    private var _uiState = MutableStateFlow(EmergencyConnectUIState.Disconnected)
    val uiState: StateFlow<EmergencyConnectUIState> = _uiState
    private var _connectionProgressText = MutableStateFlow("Resolving e-connect domain..")
    val connectionProgressText: StateFlow<String> = _connectionProgressText
    private var connectingJob: Job? = null

    init {
        logger.debug("Initializing Emergency connect view modal.")
        observeConnectionState()
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            vpnConnectionStateManager.state.collectLatest {
                logger.debug("Connection state changed to ${it.status}")
                when (it.status) {
                    Connecting -> _uiState.emit(EmergencyConnectUIState.Connecting)
                    Connected -> _uiState.emit(EmergencyConnectUIState.Connected)
                    Disconnected -> _uiState.emit(EmergencyConnectUIState.Disconnected)
                    Disconnecting -> _uiState.emit(EmergencyConnectUIState.Disconnected)
                    RequiresUserInput -> _uiState.emit(EmergencyConnectUIState.Disconnected)
                    else -> {}
                }
            }
        }
    }

    fun connectButtonClick() {
        logger.debug("User clicked connect button with current state: ${uiState.value}")
//        if (uiState.value == EmergencyConnectUIState.Connected || uiState.value == EmergencyConnectUIState.Connecting) {
//            disconnect()
//        } else {
        connect()
//        }
    }

    fun disconnect() {
        scope.launch {
            connectingJob?.cancel()
            windVpnController.disconnectAsync()
        }
    }

    // get servers from api
    private fun connect() {
        connectingJob = scope.launch {
//            _uiState.emit(EmergencyConnectUIState.Connecting)
            if(StaticData.fragmentManager != null){
                EmergencyAccountRequestDialog.show(StaticData.fragmentManager!!)
            }else{
//                _uiState.emit(EmergencyConnectUIState.Disconnected)
            }
//            windVpnController.connectUsingEmergencyProfile { progress ->
//                _connectionProgressText.value = progress
//            }.onSuccess {
//                logger.debug("Successfully connected to emergency server.")
//            }.onFailure {
//                logger.debug("Failure to connect using emergency vpn profiles: $it")
//                _uiState.emit(EmergencyConnectUIState.Disconnected)
//            }
        }
    }

    companion object {
        fun provideFactory(
                scope: CoroutineScope,
                windVpnController: WindVpnController,
                vpnConnectionStateManager: VPNConnectionStateManager
        ) = object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(EmergencyConnectViewModal::class.java)) {
                    return EmergencyConnectViewModal(
                            scope, windVpnController, vpnConnectionStateManager
                    ) as T
                }
                return super.create(modelClass)
            }
        }
    }
}