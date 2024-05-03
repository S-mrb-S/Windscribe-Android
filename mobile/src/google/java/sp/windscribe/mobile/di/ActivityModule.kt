package sp.windscribe.mobile.di

import androidx.appcompat.app.AppCompatActivity
import sp.windscribe.mobile.about.AboutView
import sp.windscribe.mobile.account.AccountView
import sp.windscribe.mobile.confirmemail.ConfirmEmailView
import sp.windscribe.mobile.connectionsettings.ConnectionSettingsView
import sp.windscribe.mobile.debug.DebugView
import sp.windscribe.mobile.email.AddEmailView
import sp.windscribe.mobile.generalsettings.GeneralSettingsView
import sp.windscribe.mobile.gpsspoofing.GpsSpoofingSettingView
import sp.windscribe.mobile.help.HelpView
import sp.windscribe.mobile.mainmenu.MainMenuView
import sp.windscribe.mobile.networksecurity.NetworkSecurityView
import sp.windscribe.mobile.networksecurity.networkdetails.NetworkDetailView
import sp.windscribe.mobile.newsfeedactivity.NewsFeedView
import sp.windscribe.mobile.robert.RobertSettingsView
import sp.windscribe.mobile.splash.SplashView
import sp.windscribe.mobile.splittunneling.SplitTunnelingView
import sp.windscribe.mobile.ticket.SendTicketView
import sp.windscribe.mobile.upgradeactivity.UpgradePresenter
import sp.windscribe.mobile.upgradeactivity.UpgradePresenterImpl
import sp.windscribe.mobile.upgradeactivity.UpgradeView
import sp.windscribe.mobile.welcome.WelcomeView
import sp.windscribe.mobile.windscribe.WindscribeView
import sp.windscribe.mobile.advance.AdvanceParamView
import sp.windscribe.vpn.ActivityInteractor
import dagger.Module
import dagger.Provides

@Module
open class ActivityModule : BaseActivityModule {
    private lateinit var upgradeView: UpgradeView
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
    constructor(mActivity: AppCompatActivity, upgradeView: UpgradeView) {
        this.activity = mActivity
        this.upgradeView = upgradeView
    }

    constructor(mActivity: AppCompatActivity, advanceParamView: AdvanceParamView) {
        this.activity = mActivity
        this.advanceParamView = advanceParamView
    }

    @Provides
    @PerActivity
    fun provideUpgradePresenter(
            activityInteractor: ActivityInteractor
    ): UpgradePresenter {
        return UpgradePresenterImpl(upgradeView, activityInteractor)
    }

    @Provides
    fun provideUpgradeView(): UpgradeView {
        return upgradeView
    }
}
