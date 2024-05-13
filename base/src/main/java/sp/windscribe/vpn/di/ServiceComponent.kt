package sp.windscribe.vpn.di

import dagger.Component
import sp.windscribe.vpn.backend.ikev2.CharonVpnServiceWrapper
import sp.windscribe.vpn.backend.openvpn.OpenVPNWrapperService
import sp.windscribe.vpn.backend.wireguard.WireGuardWrapperService
import sp.windscribe.vpn.bootreceiver.BootSessionService
import sp.windscribe.vpn.services.AutoConnectService
import sp.windscribe.vpn.services.DeviceStateService
import sp.windscribe.vpn.services.DisconnectService
import sp.windscribe.vpn.services.NetworkWhiteListService
import sp.windscribe.vpn.services.VpnTileService

@PerService
@Component(dependencies = [ApplicationComponent::class], modules = [ServiceModule::class])
interface ServiceComponent {
    fun inject(wireGuardService: WireGuardWrapperService)
    fun inject(noNetworkService: NetworkWhiteListService)
    fun inject(disconnectService: DisconnectService)
    fun inject(openVPNWrapperService: OpenVPNWrapperService)
    fun inject(deviceStateService: DeviceStateService)
    fun inject(bootSessionService: BootSessionService)
    fun inject(tileService: VpnTileService)
    fun inject(charonVpnServiceWrapper: CharonVpnServiceWrapper)
    fun inject(autoConnectService: AutoConnectService)
}
