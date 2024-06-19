package sp.windscribe.mobile.sp.util.api

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                    Log.d("EX_OP", "null")
                    withContext(Dispatchers.Main) {
                        failTo()
                    }
                    return@coroutineScope
                }
                StaticData.data = data // save data for handle protocols

                val str = ListCreator(data).createAndGet()
                state = if(str.isEmpty() || str.toString() == "null"){
                    Data.dataString = null
                    Data.settingsStorage.putString("server_cache", null)
                    2
                }else{
                    1
                }
//                longLog(str)
                Data.dataString = str // create and save
                var canSend = true

                try{
                    val cache: String = Data.settingsStorage.getString("server_cache", null).toString()
                    if(state == 1 && cache.isNotEmpty() && cache.isNotBlank()){
                        if(str.trimIndent().length == cache.trimIndent().length){
                            canSend = false
                            StaticData.canReload = false
                            Log.d("MRB", "CANT!")

                            Log.d("MRB", "CACHE: ")
                            longLog(cache)

                            Log.d("MRB", "str: ")
                            longLog(str)
                        }
                    }
                }catch (e: Exception){
                    Log.d("EX_OP", "e5 : ${e}")
                }

                // Move the view update to main thread context
                withContext(Dispatchers.Main) {
                    if (canSend) {
                        Data.static.getmViewModel().saveIsChanged(state)
                    }
                }
            } finally {
                withContext(Dispatchers.Main) {
                    navigateTo()
                }
            }
        } catch (e: Exception) {
            Log.d("EX_OP", "e4 : ${e}")
            withContext(Dispatchers.Main) {
                failTo()
            }
        }
}

fun longLog(str: String) {
    if (str.length > 4000) {
        Log.d("servers mrb", str.substring(0, 4000))
        longLog(str.substring(4000))
    } else Log.d("", str)
}