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