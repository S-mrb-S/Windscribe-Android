package sp.windscribe.vpn.di

import dagger.Module
import dagger.Provides
import sp.windscribe.vpn.api.HostType
import javax.inject.Named

@Module
class TestNetworkModule {
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
            Pair(HostType.API, "http://localhost:8080"),
            Pair(HostType.ASSET, "http://localhost:8080"),
            Pair(HostType.CHECK_IP, "http://localhost:8080")
        )
    }
}