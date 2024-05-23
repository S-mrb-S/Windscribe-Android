package sp.windscribe.mobile.sp.util.api

import com.apollographql.apollo3.api.Error
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import sp.windscribe.mobile.GetLoginQuery
import sp.windscribe.mobile.sp.api.GetLoginWithKeyQuery
import sp.windscribe.vpn.sp.Data
import sp.windscribe.vpn.sp.Static
import kotlin.concurrent.thread

suspend fun updateService(
        licenceKey: String,
        finishTo: () -> Unit,
        failTo: () -> Unit
) = coroutineScope {
    try {
        GetLoginWithKeyQuery().performWork(licenceKey,
                object : GetLoginWithKeyQuery.GetLoginCallback {

                    override fun onSuccess(data: GetLoginQuery.Data?) {
                        try{
//                            Data.static.mainApplication.run {
                                // run ViewModel on application thread
                                Data.static.getmViewModel().saveDataLeft(data?.service?.quotaLeft ?: 0)
                                Data.static.getmViewModel().saveDataDailyLeft(data?.service?.dailyQuotaLeft ?: 0)
//                            }
                        }catch (e: Exception){
                            Data.static.showToast("runtime err")
                        }
                        thread {
                            Data.serviceStorage
                                    .putString("user_name", data?.service?.name)

                            Data.serviceStorage
                                    .encode("key_login", licenceKey) // save key for get runtime data service

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

                            // usr, pss (vpn)
                            Data.serviceStorage.putString(
                                    "username_ovpn",
                                    data?.service?.username.toString()
                            )
                            Data.serviceStorage.putString(
                                    "password_ovpn",
                                    data?.service?.password.toString()
                            )
                        }.start()

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