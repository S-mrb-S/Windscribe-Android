package sp.windscribe.vpn.api

import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import sp.windscribe.vpn.BuildConfig
import sp.windscribe.vpn.constants.NetworkKeyConstants.API_ENDPOINT
import sp.windscribe.vpn.encoding.encoders.Base64
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class WindCustomApiFactory @Inject constructor(
        private val retrofitBuilder: Retrofit.Builder,
        private val okHttpClient: OkHttpClient.Builder,
) {
    private var retrofit: Retrofit? = null
    private var logger = LoggerFactory.getLogger("static_api")

    /**
     * Provide Api service interface for given url
     * @param url base url
     * @return ApiService
     */
    fun createCustomCertApi(url: String): ApiService {
        return retrofit?.newBuilder()?.baseUrl(url)
                ?.build()?.create(ApiService::class.java)!!
    }

    /**
     * Creates custom api factory to allow
     * direct ip calls in case of Api failures. Loads prepacked ca cert in to trust manager
     * ads trust manager to okhttp client.
     */
    init {
        getUnsafeOkHttpClient()?.let {
            retrofit = retrofitBuilder.baseUrl(API_ENDPOINT)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(it.build())
                    .build()
        }
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder? {
        // Load CAs from an InputStream
        val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
        if (BuildConfig.API_STATIC_CERT.isEmpty()) return null
        val caInput: InputStream = Base64.decode(BuildConfig.API_STATIC_CERT).inputStream()
        val ca: X509Certificate = caInput.use {
            cf.generateCertificate(it) as X509Certificate
        }
        System.out.println("ca=" + ca.subjectDN)

        // Create a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType).apply {
            load(null, null)
            setCertificateEntry("ca", ca)
        }

        // Create a TrustManager that trusts the CAs inputStream our KeyStore
        val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
        val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm).apply {
            init(keyStore)
        }

        // Create an SSLContext that uses our TrustManager
        val context: SSLContext = SSLContext.getInstance("TLS").apply {
            init(null, tmf.trustManagers, null)
        }

        val hostnameVerifier = HostnameVerifier { _, session ->
            HttpsURLConnection.getDefaultHostnameVerifier().run {
                logger.debug(session.peerHost)
                return@run verify("138.197.150.76", session) || verify("198.211.122.90", session)
            }
        }
        okHttpClient.hostnameVerifier(hostnameVerifier)
        val sslSocketFactory = context.socketFactory
        val trustManager = tmf.trustManagers[0] as X509TrustManager
        okHttpClient.sslSocketFactory(sslSocketFactory, trustManager)
        return okHttpClient
    }
}
