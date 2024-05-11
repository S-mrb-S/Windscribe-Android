package sp.windscribe.vpn.repository

import io.reactivex.Completable
import org.slf4j.LoggerFactory
import sp.windscribe.vpn.Windscribe.Companion.appContext
import sp.windscribe.vpn.api.IApiCallManager
import sp.windscribe.vpn.apppreference.PreferencesHelper
import sp.windscribe.vpn.constants.ApiConstants.PCP_ID
import sp.windscribe.vpn.localdatabase.LocalDbInterface
import sp.windscribe.vpn.localdatabase.tables.PopupNotificationTable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
    private val apiCallManager: IApiCallManager,
    private val localDbInterface: LocalDbInterface
) {
    private val logger = LoggerFactory.getLogger("notification_updater")
    fun update(): Completable {
        logger.debug("Starting notification data update.")
        val params = appContext.appLifeCycleObserver.pushNotificationAction?.pcpID?.let {
            hashMapOf(Pair(PCP_ID, it))
        }
        return apiCallManager.getNotifications(params)
            .flatMapCompletable { response ->
                response.dataClass?.let { newsfeed ->
                    newsfeed.notifications?.let {
                        if (it.isNotEmpty()) {
                            try {
                                localDbInterface.addToPopupNotification(
                                    PopupNotificationTable(
                                        it[0].notificationId,
                                        preferencesHelper.userName,
                                        if (it[0].popUp == 1) 1 else 0
                                    )
                                )
                            } catch (e: Exception) {
                                logger.debug("Failed add pop notification. $e")
                            }
                            return@flatMapCompletable localDbInterface.insertWindNotifications(it)
                        }
                    }
                }
                return@flatMapCompletable response.errorClass?.let {
                    logger.debug("Error updating notifications ")
                    Completable.fromAction {}
                }
            }
    }
}
