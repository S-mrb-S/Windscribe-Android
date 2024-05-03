package sp.windscribe.vpn.workers.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import sp.windscribe.vpn.Windscribe.Companion.appContext
import sp.windscribe.vpn.commonutils.Ext.result
import sp.windscribe.vpn.repository.ServerListRepository
import sp.windscribe.vpn.repository.UserRepository
import io.reactivex.Flowable
import javax.inject.Inject
import org.slf4j.LoggerFactory

class ServerListWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    private val logger = LoggerFactory.getLogger("server_list_worker")

    @Inject
    lateinit var serverListRepository: ServerListRepository
    @Inject
    lateinit var userRepository: UserRepository

    init {
        appContext.applicationComponent.inject(this)
    }

    override suspend fun doWork(): Result {
        if(!userRepository.loggedIn())return Result.failure()
        return serverListRepository.update()
                .repeatWhen {
                    val reloadServerList = serverListRepository.globalServerList && appContext.appLifeCycleObserver.overriddenCountryCode!=null
                    return@repeatWhen Flowable.just(reloadServerList)
                }.result{ success, error ->
            if(success){
                serverListRepository.load()
                logger.debug("Successfully updated server list. Global Server list: ${serverListRepository.globalServerList} CountryOverride: ${appContext.appLifeCycleObserver.overriddenCountryCode}")
            }else{
                logger.debug("Failed to update server list: $error")
            }
        }
    }
}