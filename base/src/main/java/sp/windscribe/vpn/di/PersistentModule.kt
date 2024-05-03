package sp.windscribe.vpn.di

import sp.windscribe.vpn.apppreference.AppPreferenceHelper
import sp.windscribe.vpn.apppreference.SecurePreferences
import dagger.Module
import dagger.Provides
import net.grandcentrix.tray.AppPreferences
import javax.inject.Singleton

@Module
class PersistentModule {
    @Provides
    @Singleton
    fun providesPreferenceHelper(
        mPreference: AppPreferences, securePreferences: SecurePreferences
    ): AppPreferenceHelper {
        return AppPreferenceHelper(mPreference, securePreferences)
    }
}