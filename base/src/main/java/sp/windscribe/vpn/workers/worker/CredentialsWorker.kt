package sp.windscribe.vpn.workers.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import sp.windscribe.vpn.Windscribe
import sp.windscribe.vpn.commonutils.Ext.result
import sp.windscribe.vpn.repository.ConnectionDataRepository
import sp.windscribe.vpn.repository.UserRepository
import javax.inject.Inject
import org.slf4j.LoggerFactory

class CredentialsWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    private val logger = LoggerFactory.getLogger("credentials_updater")

    @Inject
    lateinit var connectionDataRepository: ConnectionDataRepository

    @Inject
    lateinit var userRepository: UserRepository

    init {
        Windscribe.appContext.applicationComponent.inject(this)
    }

    override suspend fun doWork(): Result {
        return if(userRepository.loggedIn() && userRepository.accountStatusOkay()){
            connectionDataRepository.update().result{ success, error ->
                if(success){
                    logger.debug("Successful updated credentials data.")
                }else{
                    logger.debug("Failed to update credentials data: $error")
                }
            }
        }else{
            Result.failure()
        }
    }
}