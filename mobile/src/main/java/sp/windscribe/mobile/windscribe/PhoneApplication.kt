package sp.windscribe.mobile.windscribe

import android.content.Intent
import android.os.Handler
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.tencent.mmkv.MMKV
import de.blinkt.openvpn.core.App
import sp.windscribe.mobile.R
import sp.windscribe.mobile.connectionmode.AllProtocolFailedFragment
import sp.windscribe.mobile.connectionmode.ConnectionChangeFragment
import sp.windscribe.mobile.connectionmode.ConnectionFailureFragment
import sp.windscribe.mobile.connectionmode.DebugLogSentFragment
import sp.windscribe.mobile.connectionmode.SetupPreferredProtocolFragment
import sp.windscribe.mobile.splash.SplashActivity
import sp.windscribe.mobile.upgradeactivity.UpgradeActivity
import sp.windscribe.mobile.welcome.WelcomeActivity
import sp.windscribe.vpn.Windscribe
import sp.windscribe.vpn.Windscribe.ApplicationInterface
import sp.windscribe.vpn.autoconnection.AutoConnectionModeCallback
import sp.windscribe.vpn.autoconnection.FragmentType
import sp.windscribe.vpn.autoconnection.ProtocolInformation
import sp.windscribe.vpn.constants.PreferencesKeyConstants
import sp.windscribe.vpn.qq.Data

class PhoneApplication : Windscribe(), ApplicationInterface {
    override fun onCreate() {
        applicationInterface = this
        super.onCreate()
        MMKV.initialize(this@PhoneApplication)
        setTheme()
        Data.defaultItemDialog = Data.settingsStorage.getInt("default_connection_type", 0)
        App.setOpenVpn(this, "sp.windscribe.mobile", "spwindscribemobile", "Windscribe", false)
    }

    override val homeIntent: Intent
        get() = Intent(appContext, WindscribeActivity::class.java)
    override val splashIntent: Intent
        get() = Intent(appContext, SplashActivity::class.java)
    override val upgradeIntent: Intent
        get() = Intent(appContext, UpgradeActivity::class.java)
    override val welcomeIntent: Intent
        get() = Intent(appContext, WelcomeActivity::class.java)
    override val isTV: Boolean
        get() = false

    override fun setTheme() {
        val savedThem = preference.selectedTheme
        if (savedThem == PreferencesKeyConstants.DARK_THEME) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
    }

    override fun launchFragment(
        protocolInformationList: List<ProtocolInformation>,
        fragmentType: FragmentType,
        autoConnectionModeCallback: AutoConnectionModeCallback,
        protocolInformation: ProtocolInformation?
    ): Boolean {
        return if (activeActivity == null) {
            false
        } else {
            val viewGroup: ViewGroup =
                activeActivity?.findViewById(android.R.id.content) as ViewGroup
            if (viewGroup.children.count() > 0 && viewGroup.children.first().id != -1) {
                val fragment = when (fragmentType) {
                    FragmentType.ConnectionFailure -> ConnectionFailureFragment.newInstance(
                        protocolInformationList, autoConnectionModeCallback
                    )

                    FragmentType.ConnectionChange -> ConnectionChangeFragment.newInstance(
                        protocolInformationList, autoConnectionModeCallback
                    )

                    FragmentType.SetupAsPreferredProtocol -> SetupPreferredProtocolFragment.newInstance(
                        protocolInformation, autoConnectionModeCallback
                    )

                    FragmentType.DebugLogSent -> DebugLogSentFragment.newInstance(
                        autoConnectionModeCallback
                    )

                    FragmentType.AllProtocolFailed -> AllProtocolFailedFragment.newInstance(
                        autoConnectionModeCallback
                    )
                }
                activeActivity?.supportFragmentManager?.beginTransaction()
                    ?.setTransition(TRANSIT_FRAGMENT_FADE)
                    ?.add(viewGroup.children.first().id, fragment, "")?.commit()
            }
            true
        }
    }
}
