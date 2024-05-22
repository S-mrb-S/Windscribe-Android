package sp.windscribe.mobile.mrb.util.api

import com.apollographql.apollo3.api.Error
import kotlinx.coroutines.coroutineScope
import sp.windscribe.mobile.GetLoginQuery
import sp.windscribe.mobile.mrb.api.GetLoginWithKeyQuery
import sp.windscribe.vpn.qq.MmkvManager

suspend fun updateService(
        licenceKey: String,
        finishTo: () -> Unit,
        failTo: () -> Unit
) = coroutineScope {
    GetLoginWithKeyQuery().performWork(licenceKey,
            object : GetLoginWithKeyQuery.GetLoginCallback {

                override fun onSuccess(data: GetLoginQuery.Data?) {
                    MmkvManager.getLoginStorage().encode("key_login", licenceKey)

                    MmkvManager.getLoginStorage()
                            .putString("user_name", data?.service?.name)
                    MmkvManager.getLoginStorage().putString(
                            "reset_data",
                            data?.service?.days.toString()
                    )
                    MmkvManager.getLoginStorage().putString(
                            "username_ovpn",
                            data?.service?.username.toString()
                    )
                    MmkvManager.getLoginStorage().putString(
                            "password_ovpn",
                            data?.service?.password.toString()
                    )

                    finishTo() // get servers
                }

                override fun onFailure(errors: List<Error>?) {
                    failTo()
                }

            })
}