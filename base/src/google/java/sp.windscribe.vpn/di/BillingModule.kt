package sp.windscribe.vpn.di

import sp.windscribe.vpn.Windscribe
import sp.windscribe.vpn.billing.AmazonBillingManager
import sp.windscribe.vpn.billing.GoogleBillingManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BillingModule {
    @Provides
    @Singleton
    fun provideAmazonBillingManager(app: Windscribe): AmazonBillingManager {
        return AmazonBillingManager(app)
    }

    @Provides
    @Singleton
    fun provideGoogleBillingManager(app: Windscribe): GoogleBillingManager {
        return GoogleBillingManager(app)
    }
}