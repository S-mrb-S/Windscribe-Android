package sp.windscribe.mobile.sp.util.api

import kotlinx.coroutines.coroutineScope
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.mobile.sp.util.StaticData
import sp.windscribe.mobile.sp.util.list.ListCreator
import sp.windscribe.vpn.sp.Data

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

                val str = ListCreator(data).createAndGet()
//                longLog(str)
                Data.dataString = str // create and save
            } finally {
                Data.static.MainApplicationExecuter({
                    // run ViewModel on application thread
                    Data.static.getmViewModel().saveIsChanged(true)
                }, Data.static.mainApplication)
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