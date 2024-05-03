package sp.windscribe.vpn.di

import sp.windscribe.vpn.backend.utils.VPNPermissionActivity
import dagger.Component

@PerActivity
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(vpnPermissionActivity: VPNPermissionActivity)
}
