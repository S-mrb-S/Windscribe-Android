package sp.windscribe.mobile.mrb.util

import android.util.Log
import com.apollographql.apollo3.api.Error
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.mobile.mrb.api.GetServersWithKeyQuery
import sp.windscribe.vpn.qq.Data

suspend fun getAllServers(
    key: String,
    saveTo: (GetServersQuery.Data?) -> Unit,
    failTo: () -> Unit
) = coroutineScope {

    launch {
        try {
            GetServersWithKeyQuery().performWork(
                key,
                object : GetServersWithKeyQuery.GetServersCallback {
                    override fun onSuccess(data: GetServersQuery.Data?) {
                        try {
                            saveTo(data)
                        } catch (e: Exception) {
                            failTo()
                        }
                    }

                    override fun onFailure(errors: List<Error>?) {
                        failTo()
                    }

                })
        } catch (e: Exception) {
            failTo()
        }
    }

}

fun saveDataAndFinish(data: GetServersQuery.Data?, navigateTo: () -> Unit, failTo: () -> Unit) {
    try {
        try {
            if (data == null) {
                Data.dataString = ""
                return
            }
            val res = ListCreator(data).createAndGet()
            Data.dataString = res
        } finally {
            navigateTo()
        }
    } catch (e: Exception) {
        failTo()
    }
}

fun longLog(str: String) {
    if (str.length > 4000) {
        Log.d("servers mrb", str.substring(0, 4000))
        longLog(str.substring(4000))
    } else Log.d("", str)
}