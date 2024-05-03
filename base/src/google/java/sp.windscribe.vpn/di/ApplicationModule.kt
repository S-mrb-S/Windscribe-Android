package sp.windscribe.vpn.di

import sp.windscribe.vpn.Windscribe
import sp.windscribe.vpn.services.FirebaseManager
import sp.windscribe.vpn.services.ReceiptValidator
import sp.windscribe.vpn.services.firebasecloud.FireBaseManagerImpl
import sp.windscribe.vpn.workers.WindScribeWorkManager
import sp.windscribe.vpn.workers.worker.AmazonPendingReceiptValidator
import sp.windscribe.vpn.workers.worker.GooglePendingReceiptValidator
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
        return ReceiptValidator(windscribeApp, manager.createOneTimeWorkerRequest(AmazonPendingReceiptValidator::class.java), manager.createOneTimeWorkerRequest(GooglePendingReceiptValidator::class.java))
    }
    @Provides
    @Singleton
    fun providesFirebaseManager(): FirebaseManager {
        return FireBaseManagerImpl(windscribeApp)
    }
}
