package sp.windscribe.vpn.di

import dagger.Component
import sp.windscribe.vpn.backend.utils.VPNPermissionActivity

@PerActivity
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(vpnPermissionActivity: VPNPermissionActivity)
}
