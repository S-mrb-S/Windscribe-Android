package sp.windscribe.mobile.sp

import android.util.Log
import kotlinx.coroutines.*
import sp.windscribe.mobile.sp.util.StaticData
import sp.windscribe.mobile.sp.util.api.getAllServers
import sp.windscribe.mobile.sp.util.api.saveDataAndFinish
import sp.windscribe.mobile.sp.util.api.updateService
import sp.windscribe.mobile.sp.util.api.updateTestService

class OperationManager {
    companion object : CoroutineScope {
        private val job = SupervisorJob()
        override val coroutineContext = Dispatchers.IO + job

        // Static function to start a long-running operation
        fun startServiceOperation(
            licence: String,
            navigateTo: () -> Unit,
            failTo: (wrongKey: Boolean) -> Unit,
            justUpdateService: Boolean = false,
            test: Boolean = false,
            email: String = ""
        ) {
            launch {
                try {
                    fun fin(licence: String) {
                        if (!justUpdateService) {
                            StaticData.waiterForceReload = true

                            launch {
                                getAllServers(licence,
                                    {
                                        launch {
                                            saveDataAndFinish(it,
                                                {
                                                    navigateTo()
                                                },
                                                {
                                                    Log.d("EX_OP", "e3")
                                                    failTo(false)
                                                }
                                            )
                                        }
                                    },
                                    {
                                        Log.d("EX_OP", "e2")
                                        failTo(false)
                                    }
                                )
                            }
                        } else {
                            navigateTo()
                        }
                    }

                    if (test) {
                        updateTestService(licence, email,
                            {
                                fin(it)
                            },
                            {
                                failTo(true)
                            })
                    } else {
                        updateService(licence,
                            {
                                fin(it)
                            },
                            {
                                failTo(true)
                            })
                    }
                } catch (e: Exception) {
                    Log.d("EX_OP", "e: ${e}")
                    failTo(false)
                }
            }
        }

        // Static function to cancel all coroutines
        fun stopOperation() {
            job.cancel()
            println("Operation stopped")
        }
    }
}