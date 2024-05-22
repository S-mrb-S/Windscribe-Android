package sp.windscribe.mobile.mrb.util.api

import com.apollographql.apollo3.api.Error
import kotlinx.coroutines.coroutineScope
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.mobile.mrb.api.GetServersWithKeyQuery
import sp.windscribe.mobile.mrb.util.StaticData
import sp.windscribe.mobile.mrb.util.list.ListCreator
import sp.windscribe.vpn.qq.Data

suspend fun getAllServers(
        key: String,
        saveTo: (GetServersQuery.Data?) -> Unit,
        failTo: () -> Unit
) = coroutineScope {
    try {
        GetServersWithKeyQuery().performWork(
                key,
                object : GetServersWithKeyQuery.GetServersCallback {
                    override fun onSuccess(data: GetServersQuery.Data?) {
                        saveTo(data)
                    }

                    override fun onFailure(errors: List<Error>?) {
                        failTo()
                    }

                })
    } catch (e: Exception) {
        failTo()
    }

}

suspend fun saveDataAndFinish(data: GetServersQuery.Data?, navigateTo: () -> Unit, failTo: () -> Unit) = coroutineScope {
    try {
        try {
            if (data == null) { // data must be not null
                Data.dataString = ""
                failTo()
                return@coroutineScope
            }
            if (StaticData.data == null) {
                StaticData.data = data // cache for next time
            }

            Data.dataString = ListCreator(data).createAndGet() // create and save
        } finally {
            navigateTo()
        }
    } catch (e: Exception) {
        failTo()
    }
}

//fun longLog(str: String) {
//    if (str.length > 4000) {
//        Log.d("servers mrb", str.substring(0, 4000))
//        longLog(str.substring(4000))
//    } else Log.d("", str)
//}