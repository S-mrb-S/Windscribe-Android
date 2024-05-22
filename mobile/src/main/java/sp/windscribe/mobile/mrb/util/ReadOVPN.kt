package sp.windscribe.mobile.mrb.util

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

fun fetchOvpnConfig(url: String): String? {
    val client = OkHttpClient()

    val request = Request.Builder()
            .url(url)
            .build()

    return try {
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            response.body?.string()
        } else {
            null
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}