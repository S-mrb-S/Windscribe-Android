package sp.windscribe.vpn

import sp.windscribe.mobile.di.PerActivity
import sp.windscribe.vpn.di.ApplicationComponent
import sp.windscribe.vpn.tests.BaseTest
import dagger.Component

@PerActivity
@Component(dependencies = [ApplicationComponent::class])
interface ActivityTestComponent {
    fun inject(test: BaseTest)
}
