/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.workers.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.slf4j.LoggerFactory
import sp.windscribe.vpn.Windscribe.Companion.appContext
import sp.windscribe.vpn.commonutils.Ext.result
import sp.windscribe.vpn.repository.NotificationRepository
import sp.windscribe.vpn.repository.UserRepository
import javax.inject.Inject

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
        CoroutineWorker(context, workerParams) {

    private val logger = LoggerFactory.getLogger("notification_updater")

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject
    lateinit var userRepository: UserRepository

    init {
        appContext.applicationComponent.inject(this)
    }

    override suspend fun doWork(): Result {
        if (!userRepository.loggedIn()) return Result.failure()
        return notificationRepository.update().result { success, error ->
            if (success) {
                logger.debug("Successful updated notification data.")
            } else {
                logger.debug("Failed to update notification data: $error")
            }
        }
    }
}
