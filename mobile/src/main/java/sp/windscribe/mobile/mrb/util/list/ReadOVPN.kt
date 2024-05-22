package sp.windscribe.mobile.mrb.util.list

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

val client by lazy {
    OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()
}

fun fetchOvpnConfig(url: String): String? {
    val request = Request.Builder()
            .url(url)
            .build()

    return try {
        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                response.body?.string() ?: ""
            } else {
                ""
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}