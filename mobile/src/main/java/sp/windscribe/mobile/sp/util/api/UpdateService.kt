package sp.windscribe.mobile.sp.util.api

import android.util.Log
import com.apollographql.apollo3.api.Error
import kotlinx.coroutines.coroutineScope
import sp.windscribe.mobile.GetLoginQuery
import sp.windscribe.mobile.GetTestServerMutation
import sp.windscribe.mobile.sp.api.GetLoginWithKeyQuery
import sp.windscribe.mobile.sp.api.GetTestServersWithAndroidID
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
                        try {
                            Data.static.MainApplicationExecuter({
                                // run ViewModel on application thread
                                if(data?.service?.type?.quotaLimited == true){
                                    if(data.service.type.dailyQuotaLimited){
                                        Data.static.getmViewModel().saveDataLeft(data.service.dailyQuotaLeft
                                                ?: 0)
                                    }else{
                                        Data.static.getmViewModel().saveDataLeft(data.service.quotaLeft
                                                ?: 0)
                                    }
                                }else{
                                    Data.static.getmViewModel().saveDataLeft(99999) // ~
                                }
//                                Data.static.showToast("runtime no e")
                            }, Data.static.mainApplication)
                        } catch (e: Exception) {
                            Data.static.showToast("runtime err: ")
                            Log.d("MRBB O", e.toString())
                            failTo()
                            return
                        }
                        Data.serviceStorage
                                .putString("user_name", data?.service?.name) // plan

                        Data.serviceStorage
                                .encode("key_login", licenceKey) // save key for get runtime data service
                        try {
//                            thread {
                            if(data?.service?.type?.timeLimited == true){
                                Data.serviceStorage.putString("time_left_service", (data.service.daysLeft
                                        ?: 0).toString())
                            }else{
                                Data.serviceStorage.putString("time_left_service", "~")
                            }
                            /*
                                "timeLimited": false,
                                "quotaLimited": true,
                                "dailyQuotaLimited": false,
                            */
                            Data.serviceStorage.putBoolean(
                                    "timeLimited_service",
                                    data?.service?.type?.timeLimited ?: false
                            )
                            Data.serviceStorage.putBoolean(
                                    "quotaLimited_service",
                                    data?.service?.type?.quotaLimited ?: false
                            )
                            Data.serviceStorage.putBoolean(
                                    "dailyQuotaLimited_service",
                                    data?.service?.type?.dailyQuotaLimited ?: false
                            )// end type

                            // service info
                            Data.serviceStorage.putInt(
                                    "dailyQuota_service", // مقدار حجم کل محدودیت روزانه به مگ
                                    data?.service?.dailyQuota?.toInt() ?: 0
                            )
                            Data.serviceStorage.putInt(
                                    "quotaSum_service", // مقدار حجم کل سرویس به مگ
                                    data?.service?.quotaSum ?: 0
                            )
                            Data.serviceStorage.putInt(
                                    "daysLeft_service", // مقدار روز باقی مانده تا اتمام سرویس
                                    data?.service?.daysLeft ?: 0
                            )
                            Data.serviceStorage.putInt(
                                    "days_service", // مقدار روز سرویس
                                    data?.service?.days ?: 0
                            )

                            // ViewModel cache
                            Data.serviceStorage.putInt(
                                    "dailyQuotaLeft_service",
                                    data?.service?.dailyQuotaLeft ?: 0
                            )
                            Data.serviceStorage.putInt(
                                    "quotaLeft_service",
                                    data?.service?.quotaLeft ?: 0
                            )

                            // usr, pss (vpn)
                            Data.serviceStorage.putString(
                                    "username_ovpn",
                                    data?.service?.username.toString()
                            )
                            Data.serviceStorage.putString(
                                    "password_ovpn",
                                    data?.service?.password.toString()
                            )
//                            }.start()
                        } catch (e: Exception) {
                            Data.static.showToast("runtime err!")
                            Log.d("MRBB 6", e.toString())
                            failTo()
                            return
                        }

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

suspend fun updateTestService(
    id: String,
    email: String,
    finishTo: () -> Unit,
    failTo: () -> Unit
) = coroutineScope {
    try {
        GetTestServersWithAndroidID().performWork(id, email,
            object : GetTestServersWithAndroidID.GetLoginCallback {

                override fun onSuccess(res: GetTestServerMutation.Data?) {
                    val data = res?.createService

                    try {
                        Data.static.MainApplicationExecuter({
                            // run ViewModel on application thread
                            if(data?.service?.type?.quotaLimited == true){
                                if(data.service.type.dailyQuotaLimited){
                                    Data.static.getmViewModel().saveDataLeft(data.service.dailyQuotaLeft
                                        ?: 0)
                                }else{
                                    Data.static.getmViewModel().saveDataLeft(data.service.quotaLeft
                                        ?: 0)
                                }
                            }else{
                                Data.static.getmViewModel().saveDataLeft(99999) // ~
                            }
//                                Data.static.showToast("runtime no e")
                        }, Data.static.mainApplication)
                    } catch (e: Exception) {
                        Data.static.showToast("runtime err: ")
                        Log.d("MRBB O", e.toString())
                        failTo()
                        return
                    }
                    Data.serviceStorage
                        .putString("user_name", data?.service?.name) // plan

                    Data.serviceStorage
                        .encode("key_login", id) // save key for get runtime data service
                    Data.serviceStorage
                        .encode("email_test_login", email)
                    try {
//                            thread {
                        if(data?.service?.type?.timeLimited == true){
                            Data.serviceStorage.putString("time_left_service", (data.service.daysLeft
                                ?: 0).toString())
                        }else{
                            Data.serviceStorage.putString("time_left_service", "~")
                        }
                        /*
                            "timeLimited": false,
                            "quotaLimited": true,
                            "dailyQuotaLimited": false,
                        */
                        Data.serviceStorage.putBoolean(
                            "timeLimited_service",
                            data?.service?.type?.timeLimited ?: false
                        )
                        Data.serviceStorage.putBoolean(
                            "quotaLimited_service",
                            data?.service?.type?.quotaLimited ?: false
                        )
                        Data.serviceStorage.putBoolean(
                            "dailyQuotaLimited_service",
                            data?.service?.type?.dailyQuotaLimited ?: false
                        )// end type

                        // service info
                        Data.serviceStorage.putInt(
                            "dailyQuota_service", // مقدار حجم کل محدودیت روزانه به مگ
                            data?.service?.dailyQuota?.toInt() ?: 0
                        )
                        Data.serviceStorage.putInt(
                            "quotaSum_service", // مقدار حجم کل سرویس به مگ
                            data?.service?.quotaSum ?: 0
                        )
                        Data.serviceStorage.putInt(
                            "daysLeft_service", // مقدار روز باقی مانده تا اتمام سرویس
                            data?.service?.daysLeft ?: 0
                        )
                        Data.serviceStorage.putInt(
                            "days_service", // مقدار روز سرویس
                            data?.service?.days ?: 0
                        )

                        // ViewModel cache
                        Data.serviceStorage.putInt(
                            "dailyQuotaLeft_service",
                            data?.service?.dailyQuotaLeft ?: 0
                        )
                        Data.serviceStorage.putInt(
                            "quotaLeft_service",
                            data?.service?.quotaLeft ?: 0
                        )

                        // usr, pss (vpn)
                        Data.serviceStorage.putString(
                            "username_ovpn",
                            data?.service?.username.toString()
                        )
                        Data.serviceStorage.putString(
                            "password_ovpn",
                            data?.service?.password.toString()
                        )
//                            }.start()
                    } catch (e: Exception) {
                        Data.static.showToast("runtime err!")
                        Log.d("MRBB 6", e.toString())
                        failTo()
                        return
                    }

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