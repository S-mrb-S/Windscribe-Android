package sp.windscribe.mobile.sp.util.api

import com.apollographql.apollo3.api.Error
import kotlinx.coroutines.coroutineScope
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.mobile.sp.api.GetServersWithKeyQuery

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