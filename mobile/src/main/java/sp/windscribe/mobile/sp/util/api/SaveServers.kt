package sp.windscribe.mobile.sp.util.api

import android.util.Log
import kotlinx.coroutines.coroutineScope
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.mobile.sp.util.StaticData
import sp.windscribe.mobile.sp.util.list.ListCreator
import sp.windscribe.vpn.sp.Data

suspend fun saveDataAndFinish(data: GetServersQuery.Data?, navigateTo: () -> Unit, failTo: () -> Unit) = coroutineScope {
        try {
            Log.d("MRBF", "SAVER")
            var state = 0
            try {
                if (data == null) { // data must be not null
                    Data.dataString = null
                    failTo()
                    return@coroutineScope
                }
                StaticData.data = data // save data for handle protocols

                val str = ListCreator(data).createAndGet()
                state = if(str.isEmpty() || str.toString() == "null"){
                    Data.dataString = null
                    2
                }else{
                    1
                }
//                longLog(str)
                Data.dataString = str // create and save
                var canSend = true

                Data.static.MainApplicationExecuter({
                    // run ViewModel on application
                    val cache: String = Data.settingsStorage.getString("server_cache", null).toString()
                    if(state == 1 && cache.isNotEmpty() && cache.isNotBlank()){
                        if(str.trimIndent().length == cache.trimIndent().length){
                            Log.d("MRBB", "RETURN")
                            canSend = false
                            StaticData.canReload = false
                        }else{
                            Log.d("MRBB", "not same")
                            longLog(str.trimIndent())
                            Log.d("MRBB", "yeap")
                            longLog(cache)
                        }
                    }else{
                        Log.d("MRBB", "skipped")
                    }

                    if(canSend){
                        Data.static.getmViewModel().saveIsChanged(state)
                    }
                }, Data.static.mainApplication)
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