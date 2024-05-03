package sp.windscribe.mobile.di

import sp.windscribe.mobile.upgradeactivity.UpgradeActivity
import sp.windscribe.vpn.di.ApplicationComponent
import dagger.Component

@PerActivity
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent : BaseActivityComponent {
    fun inject(upgradeActivity: UpgradeActivity)
}