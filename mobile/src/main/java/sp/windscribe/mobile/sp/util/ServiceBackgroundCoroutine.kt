package sp.windscribe.mobile.sp.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sp.windscribe.mobile.sp.util.api.getAllServers
import sp.windscribe.mobile.sp.util.api.saveDataAndFinish
import sp.windscribe.mobile.sp.util.api.updateService

// all get data here
fun startBackgroundService(
        licence: String,
        navigateTo: () -> Unit,
        failTo: (wrongKey: Boolean) -> Unit) {
    CoroutineScope(Dispatchers.Default).launch {
        try {
            updateService(licence,
                    {
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
                    },
                    {
                        failTo(true)
                    })
        } catch (e: Exception) {
            failTo(false)
        }
    }.start()
}