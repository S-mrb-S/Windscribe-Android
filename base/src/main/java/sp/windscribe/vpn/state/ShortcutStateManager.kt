package sp.windscribe.vpn.state

import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import sp.windscribe.vpn.ServiceInteractor
import sp.windscribe.vpn.autoconnection.AutoConnectionManager
import sp.windscribe.vpn.backend.utils.WindVpnController
import sp.windscribe.vpn.model.User
import sp.windscribe.vpn.repository.UserRepository

class ShortcutStateManager(
        private var scope: CoroutineScope,
        private var userRepository: Lazy<UserRepository>,
        private var autoConnectionManager: AutoConnectionManager,
        private var networkInfoManager: NetworkInfoManager,
        private val interactor: ServiceInteractor,
        private val windVpnController: WindVpnController
) {
    private var logger = LoggerFactory.getLogger("shortcut_manager")
    private var initilized = false
    fun connect() {
        if (initilized) {
            interactor.preferenceHelper.globalUserConnectionPreference = true
            logger.debug("Connecting from shortcut.")
            windVpnController.connectAsync()
        } else {
            load {
                interactor.preferenceHelper.globalUserConnectionPreference = true
                logger.debug("Connecting from shortcut.")
                windVpnController.connectAsync()
            }
        }
    }

    fun load(callback: () -> Unit) {
        val userRepository = userRepository.get()
        scope.launch {
            logger.debug("Loading user info.")
            kotlin.runCatching {
                interactor.getUserSession()
            }.onSuccess {
                userRepository.reload(it) { user ->
                    if (user.accountStatus == User.AccountStatus.Okay) {
                        logger.debug("Loading network info.")
                        networkInfoManager.reload(false)
                        logger.debug("Loading connection info.")
                        autoConnectionManager.reset()
                        initilized = true
                        callback()
                    } else {
                        logger.debug("Account status is ${user.accountStatus}.")
                    }
                }
            }.onFailure {
                logger.debug("Unable to load user info.")
            }
        }
    }
}