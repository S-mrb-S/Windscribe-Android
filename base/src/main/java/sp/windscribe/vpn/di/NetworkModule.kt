package sp.windscribe.vpn.di

import dagger.Module
import dagger.Provides
import sp.windscribe.vpn.BuildConfig
import sp.windscribe.vpn.api.HostType
import sp.windscribe.vpn.constants.NetworkKeyConstants
import javax.inject.Named

@Module
class NetworkModule {
    @Provides
    @Named("SecondaryApiEndpointMap")
    fun providesSecondaryApiEndpointMap(): Map<HostType, String> {
        return mapOf(
                Pair(HostType.API, "https://api.totallyacdn.com"),
                Pair(HostType.ASSET, "https://assets.totallyacdn.com"),
                Pair(HostType.CHECK_IP, "https://checkip.totallyacdn.com")
        )
    }

    @Provides
    @Named("PrimaryApiEndpointMap")
    fun providesPrimaryApiEndpointMap(): Map<HostType, String> {
        return mapOf(
                Pair(HostType.API, NetworkKeyConstants.API_ENDPOINT),
                Pair(HostType.ASSET, NetworkKeyConstants.API_ENDPOINT_FOR_SERVER_LIST),
                Pair(HostType.CHECK_IP, BuildConfig.CHECK_IP_URL)
        )
    }
}