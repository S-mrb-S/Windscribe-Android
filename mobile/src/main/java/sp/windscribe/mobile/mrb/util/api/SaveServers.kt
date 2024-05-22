package sp.windscribe.mobile.mrb.util.api

import kotlinx.coroutines.coroutineScope
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.mobile.mrb.util.StaticData
import sp.windscribe.mobile.mrb.util.list.ListCreator
import sp.windscribe.vpn.qq.Data

suspend fun saveDataAndFinish(data: GetServersQuery.Data?, navigateTo: () -> Unit, failTo: () -> Unit) = coroutineScope {
    try {
        try {
            if (data == null) { // data must be not null
                Data.dataString = ""
                failTo()
                return@coroutineScope
            }
            if (StaticData.data == null) {
                StaticData.data = data // save data for handle protocols
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