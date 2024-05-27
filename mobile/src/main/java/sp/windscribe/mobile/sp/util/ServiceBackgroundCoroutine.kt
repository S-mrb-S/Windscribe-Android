package sp.windscribe.mobile.sp.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sp.windscribe.mobile.sp.util.api.getAllServers
import sp.windscribe.mobile.sp.util.api.saveDataAndFinish
import sp.windscribe.mobile.sp.util.api.updateService
import sp.windscribe.mobile.sp.util.api.updateTestService

// all get data here
fun startBackgroundService(
        licence: String,
        navigateTo: () -> Unit,
        failTo: (wrongKey: Boolean) -> Unit,
        justUpdateService: Boolean = false,
        test: Boolean = false,
        email: String = "") {
    CoroutineScope(Dispatchers.Default).launch {
        try {
            fun fin(){
                if(!justUpdateService) {
                    launch {
                        getAllServers(licence,
                            {
                                launch {
                                    saveDataAndFinish(it,
                                        {
                                            navigateTo()
                                        },
                                        {
                                            failTo(false)
                                        }
                                    )
                                }
                            },
                            {
                                failTo(false)
                            }
                        )
                    }
                }else{
                    navigateTo()
                }
            }

            if(test){
                updateTestService(licence, email,
                    {
                        fin()
                    },
                    {
                        failTo(true)
                    })
            }else{
                updateService(licence,
                    {
                        fin()
                    },
                    {
                        failTo(true)
                    })
            }
        } catch (e: Exception) {
            failTo(false)
        }
    }.start()
}
