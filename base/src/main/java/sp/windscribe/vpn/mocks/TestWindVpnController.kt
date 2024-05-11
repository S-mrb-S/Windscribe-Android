package sp.windscribe.vpn.mocks

import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import sp.windscribe.vpn.ServiceInteractor
import sp.windscribe.vpn.autoconnection.AutoConnectionManager
import sp.windscribe.vpn.autoconnection.ProtocolInformation
import sp.windscribe.vpn.backend.VPNState
import sp.windscribe.vpn.backend.VpnBackendHolder
import sp.windscribe.vpn.backend.utils.VPNProfileCreator
import sp.windscribe.vpn.backend.utils.WindVpnController
import sp.windscribe.vpn.repository.EmergencyConnectRepository
import sp.windscribe.vpn.repository.LocationRepository
import sp.windscribe.vpn.repository.UserRepository
import sp.windscribe.vpn.repository.WgConfigRepository
import sp.windscribe.vpn.state.VPNConnectionStateManager
import java.util.UUID

class TestWindVpnController(
    scope: CoroutineScope,
    interactor: ServiceInteractor,
    vpnProfileCreator: VPNProfileCreator,
    private val vpnConnectionStateManager: VPNConnectionStateManager,
    vpnBackendHolder: VpnBackendHolder,
    locationRepository: LocationRepository,
    autoConnectionManager: AutoConnectionManager,
    wgConfigRepository: WgConfigRepository,
    userRepository: Lazy<UserRepository>,
    emergencyConnectRepository: EmergencyConnectRepository
) : WindVpnController(
    scope,
    interactor,
    vpnProfileCreator,
    vpnConnectionStateManager,
    vpnBackendHolder,
    locationRepository,
    wgConfigRepository,
    userRepository,
    autoConnectionManager,
    emergencyConnectRepository
) {
    var mockState: VPNState = VPNState(VPNState.Status.Disconnected)
    override suspend fun launchVPNService(
        protocolInformation: ProtocolInformation,
        connectionId: UUID
    ) {
        delay(5000L)
        mockState.protocolInformation = protocolInformation
        vpnConnectionStateManager.setState(mockState)
    }
}