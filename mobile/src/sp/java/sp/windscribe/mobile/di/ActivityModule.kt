package sp.windscribe.mobile.di

import android.animation.ArgbEvaluator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import sp.windscribe.mobile.about.AboutPresenter
import sp.windscribe.mobile.about.AboutPresenterImpl
import sp.windscribe.mobile.about.AboutView
import sp.windscribe.mobile.account.AccountPresenter
import sp.windscribe.mobile.account.AccountPresenterImpl
import sp.windscribe.mobile.account.AccountView
import sp.windscribe.mobile.advance.AdvanceParamView
import sp.windscribe.mobile.confirmemail.ConfirmEmailPresenter
import sp.windscribe.mobile.confirmemail.ConfirmEmailPresenterImp
import sp.windscribe.mobile.confirmemail.ConfirmEmailView
import sp.windscribe.mobile.connectionsettings.ConnectionSettingsPresenter
import sp.windscribe.mobile.connectionsettings.ConnectionSettingsPresenterImpl
import sp.windscribe.mobile.connectionsettings.ConnectionSettingsView
import sp.windscribe.mobile.custom_view.CustomDialog
import sp.windscribe.mobile.debug.DebugPresenter
import sp.windscribe.mobile.debug.DebugPresenterImpl
import sp.windscribe.mobile.debug.DebugView
import sp.windscribe.mobile.email.AddEmailPresenter
import sp.windscribe.mobile.email.AddEmailPresenterImpl
import sp.windscribe.mobile.email.AddEmailView
import sp.windscribe.mobile.fragments.ServerListFragment
import sp.windscribe.mobile.generalsettings.GeneralSettingsPresenter
import sp.windscribe.mobile.generalsettings.GeneralSettingsPresenterImpl
import sp.windscribe.mobile.generalsettings.GeneralSettingsView
import sp.windscribe.mobile.gpsspoofing.GpsSpoofingPresenter
import sp.windscribe.mobile.gpsspoofing.GpsSpoofingPresenterImp
import sp.windscribe.mobile.gpsspoofing.GpsSpoofingSettingView
import sp.windscribe.mobile.help.HelpPresenter
import sp.windscribe.mobile.help.HelpPresenterImpl
import sp.windscribe.mobile.help.HelpView
import sp.windscribe.mobile.mainmenu.MainMenuPresenter
import sp.windscribe.mobile.mainmenu.MainMenuPresenterImpl
import sp.windscribe.mobile.mainmenu.MainMenuView
import sp.windscribe.mobile.networksecurity.NetworkSecurityPresenter
import sp.windscribe.mobile.networksecurity.NetworkSecurityPresenterImpl
import sp.windscribe.mobile.networksecurity.NetworkSecurityView
import sp.windscribe.mobile.networksecurity.networkdetails.NetworkDetailPresenter
import sp.windscribe.mobile.networksecurity.networkdetails.NetworkDetailPresenterImp
import sp.windscribe.mobile.networksecurity.networkdetails.NetworkDetailView
import sp.windscribe.mobile.newsfeedactivity.NewsFeedPresenter
import sp.windscribe.mobile.newsfeedactivity.NewsFeedPresenterImpl
import sp.windscribe.mobile.newsfeedactivity.NewsFeedView
import sp.windscribe.mobile.robert.RobertSettingsPresenter
import sp.windscribe.mobile.robert.RobertSettingsPresenterImpl
import sp.windscribe.mobile.robert.RobertSettingsView
import sp.windscribe.mobile.splash.SplashPresenter
import sp.windscribe.mobile.splash.SplashPresenterImpl
import sp.windscribe.mobile.splash.SplashView
import sp.windscribe.mobile.splittunneling.SplitTunnelingPresenter
import sp.windscribe.mobile.splittunneling.SplitTunnelingPresenterImpl
import sp.windscribe.mobile.splittunneling.SplitTunnelingView
import sp.windscribe.mobile.ticket.SendTicketPresenter
import sp.windscribe.mobile.ticket.SendTicketPresenterImpl
import sp.windscribe.mobile.ticket.SendTicketView
import sp.windscribe.mobile.utils.PermissionManager
import sp.windscribe.mobile.utils.PermissionManagerImpl
import sp.windscribe.mobile.welcome.WelcomePresenter
import sp.windscribe.mobile.welcome.WelcomePresenterImpl
import sp.windscribe.mobile.welcome.WelcomeView
import sp.windscribe.mobile.welcome.viewmodal.EmergencyConnectViewModal
import sp.windscribe.mobile.windscribe.WindscribePresenter
import sp.windscribe.mobile.windscribe.WindscribePresenterImpl
import sp.windscribe.mobile.windscribe.WindscribeView
import sp.windscribe.vpn.ActivityInteractor
import sp.windscribe.vpn.ActivityInteractorImpl
import sp.windscribe.vpn.api.IApiCallManager
import sp.windscribe.vpn.apppreference.PreferencesHelper
import sp.windscribe.vpn.autoconnection.AutoConnectionManager
import sp.windscribe.vpn.backend.TrafficCounter
import sp.windscribe.vpn.backend.utils.WindVpnController
import sp.windscribe.vpn.decoytraffic.DecoyTrafficController
import sp.windscribe.vpn.localdatabase.LocalDbInterface
import sp.windscribe.vpn.repository.*
import sp.windscribe.vpn.state.NetworkInfoManager
import sp.windscribe.vpn.state.PreferenceChangeObserver
import sp.windscribe.vpn.state.VPNConnectionStateManager
import sp.windscribe.vpn.workers.WindScribeWorkManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import javax.inject.Named

@Module
open class ActivityModule: BaseActivityModule {
    constructor()
    constructor(activity: AppCompatActivity) {
        this.activity = activity
    }

    constructor(mActivity: AppCompatActivity, robertSettingsView: RobertSettingsView) {
        this.activity = mActivity
        this.robertSettingsView = robertSettingsView
    }

    constructor(mActivity: AppCompatActivity, confirmEmailView: ConfirmEmailView) {
        this.activity = mActivity
        this.confirmEmailView = confirmEmailView
    }

    constructor(mActivity: AppCompatActivity, helpView: HelpView) {
        this.activity = mActivity
        this.helpView = helpView
    }

    constructor(mActivity: AppCompatActivity, advanceParamView: AdvanceParamView) {
        this.activity = mActivity
        this.advanceParamView = advanceParamView
    }

    constructor(mActivity: AppCompatActivity, sendTicketView: SendTicketView) {
        this.activity = mActivity
        this.sendTicketView = sendTicketView
    }

    constructor(activity: AppCompatActivity, welcomeView: WelcomeView) {
        this.activity = activity
        this.welcomeView = welcomeView
    }

    constructor(mActivity: AppCompatActivity, splashView: SplashView) {
        this.activity = mActivity
        this.splashView = splashView
    }

    constructor(mActivity: AppCompatActivity, windscribeView: WindscribeView) {
        this.activity = mActivity
        this.windscribeView = windscribeView
    }

    constructor(mActivity: AppCompatActivity, networkDetailView: NetworkDetailView) {
        this.activity = mActivity
        this.networkDetailView = networkDetailView
    }

    constructor(mActivity: AppCompatActivity, mainMenuView: MainMenuView) {
        this.activity = mActivity
        this.mainMenuView = mainMenuView
    }

    constructor(mActivity: AppCompatActivity, generalSettingsView: GeneralSettingsView) {
        this.activity = mActivity
        this.generalSettingsView = generalSettingsView
    }

    constructor(mActivity: AppCompatActivity, networkSecurityView: NetworkSecurityView) {
        this.activity = mActivity
        this.networkSecurityView = networkSecurityView
    }

    constructor(mActivity: AppCompatActivity, accountView: AccountView) {
        this.activity = mActivity
        this.accountView = accountView
    }

    constructor(mActivity: AppCompatActivity, newsFeedView: NewsFeedView) {
        this.activity = mActivity
        this.newsFeedView = newsFeedView
    }

    constructor(mActivity: AppCompatActivity, emailView: AddEmailView) {
        this.activity = mActivity
        this.emailView = emailView
    }

    constructor(mActivity: AppCompatActivity, connectionSettingsView: ConnectionSettingsView) {
        this.activity = mActivity
        this.connectionSettingsView = connectionSettingsView
    }

    constructor(mActivity: AppCompatActivity, splitTunnelingView: SplitTunnelingView) {
        this.activity = mActivity
        this.splitTunnelingView = splitTunnelingView
    }

    constructor(mActivity: AppCompatActivity, gpsSpoofingSettingView: GpsSpoofingSettingView) {
        this.activity = mActivity
        this.gpsSpoofingSettingView = gpsSpoofingSettingView
    }

    constructor(mActivity: AppCompatActivity, debugView: DebugView) {
        this.activity = mActivity
        this.debugView = debugView
    }

    constructor(mActivity: AppCompatActivity, aboutView: AboutView) {
        this.activity = mActivity
        this.aboutView = aboutView
    }
}