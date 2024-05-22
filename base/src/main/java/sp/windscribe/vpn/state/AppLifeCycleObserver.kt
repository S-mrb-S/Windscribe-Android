/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.state

import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import sp.windscribe.vpn.Windscribe.Companion.appContext
import sp.windscribe.vpn.Windscribe.Companion.applicationScope
import sp.windscribe.vpn.api.ApiCallType
import sp.windscribe.vpn.api.DomainFailOverManager
import sp.windscribe.vpn.api.response.PushNotificationAction
import sp.windscribe.vpn.workers.WindScribeWorkManager
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

/**
Tracks App life cycle
 * */
@Singleton
class AppLifeCycleObserver @Inject constructor(
        private val workManager: WindScribeWorkManager,
        private val networkInfoManager: NetworkInfoManager,
        private val domainFailOverManager: DomainFailOverManager
) :
        LifecycleObserver {

    private val logger = LoggerFactory.getLogger("app")
    private val startingFresh = AtomicBoolean(false)
    var overriddenCountryCode: String? = null
    private var _appActivationState = MutableStateFlow(false)
    val appActivationState: StateFlow<Boolean> = _appActivationState
    private var pushNotification: PushNotificationAction? = null
    var pushNotificationAction: PushNotificationAction?
        get() = pushNotification
        set(value) {
            pushNotification = value
            appContext.workManager.updateNotifications()
        }

    @OnLifecycleEvent(ON_CREATE)
    fun createApp() {
        startingFresh.set(true)
    }

    @OnLifecycleEvent(ON_PAUSE)
    fun pausingApp() {
        isInForeground = false
        workManager.onAppMovedToBackground()
        logger.debug("----------App going to background.--------\n")
    }

    @OnLifecycleEvent(ON_RESUME)
    fun resumingApp() {
        if (startingFresh.get().not()) {
            logger.debug("----------------App moved to Foreground.------------\n")
        }
        if (appContext.vpnConnectionStateManager.isVPNConnected().not()) {
            logger.debug("Resetting server list country code.")
            overriddenCountryCode = null
        }
        domainFailOverManager.reset(ApiCallType.Other)
        networkInfoManager.reload()
        if (startingFresh.getAndSet(false)) {
            isInForeground = false
            workManager.onAppStart()
        } else {
            isInForeground = true
            workManager.onAppMovedToForeground()
        }
        applicationScope.launch {
            _appActivationState.emit(_appActivationState.value.not())
        }
    }

    companion object {
        var isInForeground = false
    }
}
