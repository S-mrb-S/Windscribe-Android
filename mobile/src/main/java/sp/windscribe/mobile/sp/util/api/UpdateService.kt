package sp.windscribe.mobile.sp.util.api

import com.apollographql.apollo3.api.Error
import kotlinx.coroutines.coroutineScope
import sp.windscribe.mobile.GetLoginQuery
import sp.windscribe.mobile.sp.api.GetLoginWithKeyQuery
import sp.windscribe.vpn.sp.Data

suspend fun updateService(
        licenceKey: String,
        finishTo: () -> Unit,
        failTo: () -> Unit
) = coroutineScope {
    try {
        GetLoginWithKeyQuery().performWork(licenceKey,
                object : GetLoginWithKeyQuery.GetLoginCallback {

                    override fun onSuccess(data: GetLoginQuery.Data?) {
                        Data.serviceStorage
                                .encode("key_login", licenceKey) // save key for get runtime data service

                        Data.serviceStorage
                                .putString("user_name", data?.service?.name)

                        Data.serviceStorage.putString(
                                "reset_data",
                                data?.service?.days.toString()
                        )
                        Data.serviceStorage.putString(
                                "username_ovpn",
                                data?.service?.username.toString()
                        )
                        Data.serviceStorage.putString(
                                "password_ovpn",
                                data?.service?.password.toString()
                        )

                        finishTo() // get servers
                    }

                    override fun onFailure(errors: List<Error>?) {
                        failTo()
                    }

                })
    } catch (e: Exception) {
        failTo()
    }
}