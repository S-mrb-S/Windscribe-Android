package sp.windscribe.vpn.api

import android.net.Uri
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import sp.windscribe.vpn.Windscribe
import sp.windscribe.vpn.backend.wireguard.WireguardBackend
import sp.windscribe.vpn.constants.NetworkKeyConstants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WindApiFactory @Inject constructor(
        retrofitBuilder: Retrofit.Builder,
        private val okHttpClient: Builder,
        private val dnsResolver: WindscribeDnsResolver,
        private val protectedApiFactory: ProtectedApiFactory
) {

    private val mRetrofit: Retrofit
    fun createApi(url: String, protect: Boolean = false, ip: String? = null): ApiService {
        val activeBackend = Windscribe.appContext.vpnController.vpnBackendHolder.activeBackend
        if (protect && activeBackend is WireguardBackend && activeBackend.active) {
            return protectedApiFactory.createApi(url)
        }
        // if ip is already available add it to resolver cache
        if (ip != null) {
            val host = Uri.parse(url).host
            if (host != null) {
                dnsResolver.addToCache(host, ip)
                return mRetrofit.newBuilder().baseUrl(url)
                        .client(okHttpClient.dns(dnsResolver).build())
                        .build().create(ApiService::class.java)
            }
        }
        return mRetrofit.newBuilder().baseUrl(url).build().create(ApiService::class.java)
    }

    init {
        mRetrofit = retrofitBuilder.baseUrl(NetworkKeyConstants.API_ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient.build()).build()
    }
}
