/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.di

import sp.windscribe.vpn.Windscribe
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, TestNetworkModule::class, TestVPNModule::class, TestPersistentModule::class])
interface ApplicationTestComponent : ApplicationComponent {
    override fun inject(app: Windscribe)
}