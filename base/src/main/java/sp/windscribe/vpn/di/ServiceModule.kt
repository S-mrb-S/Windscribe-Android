package sp.windscribe.vpn.di

import dagger.Module
import dagger.Provides
import sp.windscribe.vpn.ServiceInteractor
import sp.windscribe.vpn.ServiceInteractorImpl
import sp.windscribe.vpn.api.IApiCallManager
import sp.windscribe.vpn.apppreference.PreferencesHelper
import sp.windscribe.vpn.localdatabase.LocalDbInterface

@Module
class ServiceModule {
    @Provides
    @PerService
    fun providesServiceInteractor(
        preferencesHelper: PreferencesHelper,
        apiCallManager: IApiCallManager,
        localDbInterface: LocalDbInterface
    ): ServiceInteractor {
        return ServiceInteractorImpl(preferencesHelper, apiCallManager, localDbInterface)
    }
}
