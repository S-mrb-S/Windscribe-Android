/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn

import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import android.os.StrictMode.VmPolicy
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import de.blinkt.openvpn.core.PRNGFixes
import io.reactivex.plugins.RxJavaPlugins
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.conscrypt.Conscrypt
import org.slf4j.LoggerFactory
import org.strongswan.android.logic.StrongSwanApplication
import sp.windscribe.vpn.apppreference.PreferencesHelper
import sp.windscribe.vpn.autoconnection.AutoConnectionModeCallback
import sp.windscribe.vpn.autoconnection.FragmentType
import sp.windscribe.vpn.autoconnection.ProtocolInformation
import sp.windscribe.vpn.backend.ikev2.CharonVpnServiceWrapper
import sp.windscribe.vpn.backend.ikev2.StrongswanCertificateManager.init
import sp.windscribe.vpn.backend.utils.WindVpnController
import sp.windscribe.vpn.constants.PreferencesKeyConstants
import sp.windscribe.vpn.di.ActivityComponent
import sp.windscribe.vpn.di.ApplicationComponent
import sp.windscribe.vpn.di.ApplicationModule
import sp.windscribe.vpn.di.DaggerActivityComponent
import sp.windscribe.vpn.di.DaggerApplicationComponent
import sp.windscribe.vpn.di.DaggerServiceComponent
import sp.windscribe.vpn.di.ServiceComponent
import sp.windscribe.vpn.di.ServiceModule
import sp.windscribe.vpn.localdatabase.WindscribeDatabase
import sp.windscribe.vpn.mocklocation.MockLocationManager
import sp.windscribe.vpn.services.FirebaseManager
import sp.windscribe.vpn.services.canAccessNetworkName
import sp.windscribe.vpn.services.startAutoConnectService
import sp.windscribe.vpn.state.AppLifeCycleObserver
import sp.windscribe.vpn.state.DeviceStateManager
import sp.windscribe.vpn.state.VPNConnectionStateManager
import sp.windscribe.vpn.workers.WindScribeWorkManager
import java.security.Security
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

open class Windscribe : MultiDexApplication() {
    /**
     * Base Module use this interface to access activity
     * from ui modules.
     */
    interface ApplicationInterface {
        val homeIntent: Intent
        val welcomeIntent: Intent
        val splashIntent: Intent
        val upgradeIntent: Intent
        val isTV: Boolean
        fun setTheme()
        fun launchFragment(
            protocolInformationList: List<ProtocolInformation>,
            fragmentType: FragmentType,
            autoConnectionModeCallback: AutoConnectionModeCallback,
            protocolInformation: ProtocolInformation? = null
        ): Boolean
    }

    private val logger = LoggerFactory.getLogger("Windscribe")
    var activeActivity: AppCompatActivity? = null
    lateinit var applicationInterface: ApplicationInterface

    @Inject
    lateinit var preference: PreferencesHelper

    @Inject
    lateinit var appLifeCycleObserver: AppLifeCycleObserver

    @Inject
    lateinit var deviceStateManager: DeviceStateManager

    @Inject
    lateinit var workManager: WindScribeWorkManager

    @Inject
    lateinit var windscribeDatabase: WindscribeDatabase

    @Inject
    lateinit var firebaseManager: FirebaseManager

    @Inject
    lateinit var vpnConnectionStateManager: VPNConnectionStateManager

    @Inject
    lateinit var mockLocationManager: MockLocationManager

    @Inject
    lateinit var vpnController: WindVpnController
    lateinit var applicationComponent: ApplicationComponent
    lateinit var activityComponent: ActivityComponent
    lateinit var serviceComponent: ServiceComponent

    override fun onCreate() {
        if (BuildConfig.DEV) {
            setupStrictMode()
        }
        super.onCreate()
        appContext = this
        setupConscrypt()
        registerForegroundActivityObserver()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        applicationComponent = getApplicationModuleComponent()
        applicationComponent.inject(this)
        activityComponent = DaggerActivityComponent.builder()
            .applicationComponent(applicationComponent)
            .build()
        serviceComponent = serviceComponent()
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifeCycleObserver)
        preference.isNewApplicationInstance = true
        WindContextWrapper.setAppLocale(this)
        try {
            PRNGFixes.apply()
        } catch (ignored: Exception) {
        }
        setUpNewInstallation()
        RxJavaPlugins.setErrorHandler { throwable: Throwable -> logger.info(throwable.toString()) }
        initStrongswan()
        if (BuildConfig.APP_ID.isNotEmpty()) {
            firebaseManager.initialise()
        }
        deviceStateManager.init(this)
        if (preference.sessionHash != null) {
            workManager.updateNodeLatencies()
        }
        mockLocationManager.init()
        if (preference.sessionHash != null && preference.autoConnect && canAccessNetworkName()) {
            startAutoConnectService()
        }
    }

    private fun initStrongswan() {
        StrongSwanApplication.setContext(applicationContext)
        StrongSwanApplication.setService(CharonVpnServiceWrapper::class.java)
        init(this)
    }

    private fun languageToCode(language: String): String {
        val firstIndex = language.indexOfLast { it == "(".single() }
        return language.substring(firstIndex + 1, language.length - 1)
    }

    fun getAppSupportedSystemLanguage(): String {
        val languageCode = if (VERSION.SDK_INT >= VERSION_CODES.N) {
            resources.configuration.locales.get(0).language
        } else {
            resources.configuration.locale.language
        }
        return appContext.resources.getStringArray(R.array.language).firstOrNull {
            languageCode == languageToCode(it)
        } ?: PreferencesKeyConstants.DEFAULT_LANGUAGE
    }

    fun getSavedLocale(): Locale {
        val selectedLanguage = appContext.preference.savedLanguage
        val firstIndex = selectedLanguage.indexOfLast { it == "(".single() }
        val language = selectedLanguage.substring(firstIndex + 1, selectedLanguage.length - 1)
        return if (language.contains("-")) {
            val splits = language.split("-")
            Locale(splits[0], splits[1])
        } else {
            Locale(language)
        }
    }

    private fun setUpNewInstallation() {
        if (preference.getResponseString(PreferencesKeyConstants.NEW_INSTALLATION) == null) {
            preference.saveResponseStringData(
                PreferencesKeyConstants.NEW_INSTALLATION,
                PreferencesKeyConstants.I_OLD
            )
            // This will be true for legacy app but not beta version users
            if (preference.getResponseString(PreferencesKeyConstants.CONNECTION_STATUS) == null) {
                // Only Recording for legacy to new version
                preference.saveResponseStringData(
                    PreferencesKeyConstants.NEW_INSTALLATION,
                    PreferencesKeyConstants.I_NEW
                )
                preference.removeResponseData(PreferencesKeyConstants.SESSION_HASH)
            }
        }
    }

    /**
     * Strict mode log thread and vm violations in Dev environment.
     * */
    private fun setupStrictMode() {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            StrictMode.setThreadPolicy(
                Builder()
                    .detectAll()
                    .permitDiskReads()
                    .permitDiskWrites()
                    .permitUnbufferedIo()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects().detectActivityLeaks().detectFileUriExposure()
                    .detectLeakedRegistrationObjects().detectContentUriWithoutPermission()
                    .penaltyLog().build()
            )
        }
    }

    private fun setupConscrypt() {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            Security.insertProviderAt(
                Conscrypt.newProviderBuilder().defaultTlsProtocol("TLSv1.3").build(), 1
            )
            Security.removeProvider("AndroidOpenSSL")
        }
    }

    companion object {
        @JvmStatic
        fun getExecutorService(): ExecutorService {
            return Executors.newSingleThreadExecutor()
        }

        /**
         * Provides access to global context.
         */
        @JvmStatic
        lateinit var appContext: Windscribe

        var applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    open fun getApplicationModuleComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this)).build()
    }

    private fun serviceComponent(): ServiceComponent {
        return DaggerServiceComponent.builder()
            .serviceModule(ServiceModule())
            .applicationComponent(applicationComponent)
            .build()
    }

    override fun onLowMemory() {
        logger.debug("Device is running low on memory.")
        super.onLowMemory()
    }

    override fun onTerminate() {
        logger.debug("App is being terminated.")
        super.onTerminate()
    }

    override fun onTrimMemory(level: Int) {
        if (level > 60)
            logger.debug("Device is asking for memory trim with level = $level.")
        super.onTrimMemory(level)
    }

    private fun registerForegroundActivityObserver() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleObserver {
            override fun onActivityResumed(activity: Activity) {
                activeActivity = activity as? AppCompatActivity
            }

            override fun onActivityPaused(activity: Activity) {
                activeActivity = null
            }
        })
    }
}
