package sp.windscribe.mobile.mrb.util

import android.util.Log
import com.apollographql.apollo3.api.Error
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.mobile.mrb.api.GetServersWithKeyQuery
import sp.windscribe.vpn.qq.Data
import sp.windscribe.vpn.qq.MmkvManager

suspend fun getAllServers(key:String, saveTo: (GetServersQuery.Data?) -> Unit, failTo: () -> Unit) = coroutineScope {

    launch {
        try{
            GetServersWithKeyQuery().performWork(
                key,
                object : GetServersWithKeyQuery.GetServersCallback {
                    override fun onSuccess(data: GetServersQuery.Data?) {
                        try{
                            saveTo(data)
                        }catch (e:Exception){
                            Log.d("MRT", "ERR catch2: " + e.toString())
                            failTo()
                        }
                    }

                    override fun onFailure(errors: List<Error>?) {
                        Log.d("MRT", "ERR failer" + errors.toString())
                        failTo()
                    }

                })
        }catch (e: Exception){
            Log.d("MRT", "ERR catch: " + e.toString())
            failTo()
        }
    }

}

fun saveDataAndFinish(data: GetServersQuery.Data?, navigateTo: () -> Unit, failTo: () -> Unit) {
    try {
        try {
            Data.dataString = createExample()
        } finally {
            navigateTo()
        }
    } catch (e: Exception) {
        failTo()
    }
}