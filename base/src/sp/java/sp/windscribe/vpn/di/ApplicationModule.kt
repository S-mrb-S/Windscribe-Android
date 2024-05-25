package sp.windscribe.vpn.di

import sp.windscribe.vpn.Windscribe
import sp.windscribe.vpn.services.FirebaseManager
import sp.windscribe.vpn.services.ReceiptValidator
import sp.windscribe.vpn.services.firebasecloud.FirebaseManagerImpl
import sp.windscribe.vpn.workers.WindScribeWorkManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Application module provides production dependencies
 * In future plan is break this module in to smaller modules
 * to ease swapping of modules for testing.
 * */
@Module
class ApplicationModule(override var windscribeApp: Windscribe) : BaseApplicationModule() {
    @Provides
    @Singleton
    fun provideReceiptValidator(manager: WindScribeWorkManager): ReceiptValidator {
        return ReceiptValidator(windscribeApp, null, null)
    }
    @Provides
    @Singleton
    fun providesFirebaseManager(): FirebaseManager {
        return FirebaseManagerImpl(windscribeApp)
    }
}