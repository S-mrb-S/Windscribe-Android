package sp.windscribe.vpn.di

import dagger.Lazy
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import sp.windscribe.vpn.ServiceInteractor
import sp.windscribe.vpn.autoconnection.AutoConnectionManager
import sp.windscribe.vpn.backend.VpnBackendHolder
import sp.windscribe.vpn.backend.utils.VPNProfileCreator
import sp.windscribe.vpn.backend.utils.WindVpnController
import sp.windscribe.vpn.mocks.TestWindVpnController
import sp.windscribe.vpn.repository.EmergencyConnectRepository
import sp.windscribe.vpn.repository.LocationRepository
import sp.windscribe.vpn.repository.UserRepository
import sp.windscribe.vpn.repository.WgConfigRepository
import sp.windscribe.vpn.state.VPNConnectionStateManager
import javax.inject.Singleton

@Module
class TestVPNModule {
    @Provides
    @Singleton
    fun provideWindVpnController(
        coroutineScope: CoroutineScope,
        serviceInteractor: ServiceInteractor,
        vpnProfileCreator: VPNProfileCreator,
        autoConnectionManager: AutoConnectionManager,
        VPNConnectionStateManager: VPNConnectionStateManager,
        vpnBackendHolder: VpnBackendHolder,
        locationRepository: LocationRepository,
        wgConfigRepository: WgConfigRepository,
        userRepository: Lazy<UserRepository>,
        emergencyConnectRepository: EmergencyConnectRepository
    ): WindVpnController {
        return TestWindVpnController(
            coroutineScope,
            serviceInteractor,
            vpnProfileCreator,
            VPNConnectionStateManager,
            vpnBackendHolder,
            locationRepository,
            autoConnectionManager,
            wgConfigRepository,
            userRepository,
            emergencyConnectRepository
        )
    }
}