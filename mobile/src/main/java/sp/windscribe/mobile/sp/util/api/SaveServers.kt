package sp.windscribe.mobile.sp.util.api

import kotlinx.coroutines.coroutineScope
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.mobile.sp.util.StaticData
import sp.windscribe.mobile.sp.util.list.ListCreator
import sp.windscribe.vpn.sp.Data

suspend fun saveDataAndFinish(data: GetServersQuery.Data?, navigateTo: () -> Unit, failTo: () -> Unit) = coroutineScope {
        try {
            var state = 0
            try {
                if (data == null) { // data must be not null
                    Data.dataString = ""
                    failTo()
                    return@coroutineScope
                }
                StaticData.data = data // save data for handle protocols

                val str = ListCreator(data).createAndGet()
                state = if(str.isEmpty() || str.toString() == "null"){
                    2
                }else{
                    1
                }
//                longLog(str)
                Data.dataString = str // create and save
            } finally {
                Data.static.MainApplicationExecuter({
                    // run ViewModel on application thread
                    Data.static.getmViewModel().saveIsChanged(state)
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