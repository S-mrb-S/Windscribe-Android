package sp.windscribe.vpn.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Dns
import okio.ByteString.Companion.encodeUtf8
import sp.windscribe.vpn.apppreference.PreferencesHelper
import java.net.InetAddress
import java.net.UnknownHostException

class WindscribeDnsResolver(
        private val mainScope: CoroutineScope,
        private val preferenceHelper: PreferencesHelper
) : Dns {
    var memoryCache = mutableMapOf<String, String>()

    fun addToCache(host: String, ip: String) {
        memoryCache[host] = ip
    }

    override fun lookup(hostname: String): List<InetAddress> {
        val key = hostname.encodeUtf8().md5().hex()
        try {
            if (memoryCache.containsKey(hostname)) {
                return listOf(InetAddress.getByName(memoryCache[hostname]))
            }
            val addresses = InetAddress.getAllByName(hostname).toList()
            cacheDnsResponse(key, addresses)
            return addresses
        } catch (e: UnknownHostException) {
            return preferenceHelper.getResponseString(key)?.let {
                val type = object : TypeToken<List<InetAddress>>() {}.type
                return Gson().fromJson(it, type)
            } ?: throw UnknownHostException("Broken system behaviour for dns lookup of $hostname")
        } catch (e: Exception) {
            throw UnknownHostException("Broken system behaviour for dns lookup of $hostname").apply {
                initCause(e)
            }
        }
    }

    private fun cacheDnsResponse(key: String, addresses: List<InetAddress>) {
        mainScope.launch {
            preferenceHelper.saveResponseStringData(key, Gson().toJson(addresses))
        }
    }
}