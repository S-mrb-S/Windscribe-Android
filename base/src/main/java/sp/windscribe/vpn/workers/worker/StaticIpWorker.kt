package sp.windscribe.vpn.workers.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sp.windscribe.vpn.Windscribe.Companion.appContext
import sp.windscribe.vpn.commonutils.Ext.result
import sp.windscribe.vpn.repository.StaticIpRepository
import sp.windscribe.vpn.repository.UserRepository
import javax.inject.Inject

class StaticIpWorker(context: Context, params: WorkerParameters) :
        CoroutineWorker(context, params) {
    @Inject
    lateinit var staticIpRepository: StaticIpRepository

    @Inject
    lateinit var userRepository: UserRepository

    val logger: Logger = LoggerFactory.getLogger("static_ip_repo")

    init {
        appContext.applicationComponent.inject(this)
    }

    override suspend fun doWork(): Result {
        if (!userRepository.loggedIn()) return Result.failure()
        return staticIpRepository.update().result { success, error ->
            if (success) {
                staticIpRepository.load()
                logger.debug("Successfully updated static ip list.")
            } else {
                logger.debug("Failed to update static ip list.: $error")
            }
        }
    }
}