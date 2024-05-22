package sp.windscribe.mobile.di

import android.animation.ArgbEvaluator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import sp.windscribe.mobile.about.AboutPresenter
import sp.windscribe.mobile.about.AboutPresenterImpl
import sp.windscribe.mobile.about.AboutView
import sp.windscribe.mobile.account.AccountPresenter
import sp.windscribe.mobile.account.AccountPresenterImpl
import sp.windscribe.mobile.account.AccountView
import sp.windscribe.mobile.advance.AdvanceParamPresenter
import sp.windscribe.mobile.advance.AdvanceParamView
import sp.windscribe.mobile.advance.AdvanceParamsPresenterImpl
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
import sp.windscribe.vpn.repository.ConnectionDataRepository
import sp.windscribe.vpn.repository.LatencyRepository
import sp.windscribe.vpn.repository.LocationRepository
import sp.windscribe.vpn.repository.NotificationRepository
import sp.windscribe.vpn.repository.ServerListRepository
import sp.windscribe.vpn.repository.StaticIpRepository
import sp.windscribe.vpn.repository.UserRepository
import sp.windscribe.vpn.services.FirebaseManager
import sp.windscribe.vpn.services.ReceiptValidator
import sp.windscribe.vpn.state.NetworkInfoManager
import sp.windscribe.vpn.state.PreferenceChangeObserver
import sp.windscribe.vpn.state.VPNConnectionStateManager
import sp.windscribe.vpn.workers.WindScribeWorkManager
import javax.inject.Named

@Module
open class BaseActivityModule {
    lateinit var activity: AppCompatActivity
    lateinit var confirmEmailView: ConfirmEmailView
    lateinit var helpView: HelpView
    lateinit var aboutView: AboutView
    lateinit var accountView: AccountView
    lateinit var connectionSettingsView: ConnectionSettingsView
    lateinit var emailView: AddEmailView
    lateinit var generalSettingsView: GeneralSettingsView
    lateinit var gpsSpoofingSettingView: GpsSpoofingSettingView
    lateinit var mainMenuView: MainMenuView
    lateinit var networkDetailView: NetworkDetailView
    lateinit var networkSecurityView: NetworkSecurityView
    lateinit var newsFeedView: NewsFeedView
    lateinit var robertSettingsView: RobertSettingsView
    lateinit var splashView: SplashView
    lateinit var splitTunnelingView: SplitTunnelingView
    lateinit var windscribeView: WindscribeView
    lateinit var sendTicketView: SendTicketView
    lateinit var welcomeView: WelcomeView
    lateinit var debugView: DebugView
    lateinit var advanceParamView: AdvanceParamView

    @Provides
    fun provideAboutPresenter(activityInteractor: ActivityInteractor): AboutPresenter {
        return AboutPresenterImpl(aboutView, activityInteractor)
    }

    @Provides
    fun provideDebugPresenter(activityInteractor: ActivityInteractor): DebugPresenter {
        return DebugPresenterImpl(debugView, activityInteractor)
    }

    @Provides
    fun provideAccountPresenter(activityInteractor: ActivityInteractor): AccountPresenter {
        return AccountPresenterImpl(accountView, activityInteractor)
    }

    @Provides
    fun provideAccountView(): AccountView {
        return accountView
    }

    @Provides
    fun provideDebugView(): DebugView {
        return debugView
    }

    @Provides
    fun provideAdvanceParamsView(): AdvanceParamView {
        return advanceParamView
    }

    @Provides
    fun provideActivity(): AppCompatActivity {
        return activity
    }

    @Provides
    fun provideAddEmailPresenter(activityInteractor: ActivityInteractor): AddEmailPresenter {
        return AddEmailPresenterImpl(emailView, activityInteractor)
    }

    @Provides
    fun provideConfirmEmailPresenter(
            confirmEmailView: ConfirmEmailView, activityInteractor: ActivityInteractor
    ): ConfirmEmailPresenter {
        return ConfirmEmailPresenterImp(confirmEmailView, activityInteractor)
    }

    @Provides
    fun provideConfirmEmailView(): ConfirmEmailView {
        return confirmEmailView
    }

    @Provides
    fun provideConnectionPresenter(
            activityInteractor: ActivityInteractor,
            permissionManager: PermissionManager
    ): ConnectionSettingsPresenter {
        return ConnectionSettingsPresenterImpl(
                connectionSettingsView,
                activityInteractor,
                permissionManager
        )
    }

    @Provides
    fun provideConnectionSettingsView(): ConnectionSettingsView {
        return connectionSettingsView
    }

    @Provides
    fun provideCustomDialog(): CustomDialog {
        return CustomDialog(activity)
    }

    @Provides
    fun provideEmailView(): AddEmailView {
        return emailView
    }

    @Provides
    fun provideGeneralSettingsPresenter(
            activityInteractor: ActivityInteractor
    ): GeneralSettingsPresenter {
        return GeneralSettingsPresenterImpl(generalSettingsView, activityInteractor)
    }

    @Provides
    fun provideGeneralSettingsView(): GeneralSettingsView {
        return generalSettingsView
    }

    @Provides
    fun provideGpsSpoofingPresenter(activityInteractor: ActivityInteractor): GpsSpoofingPresenter {
        return GpsSpoofingPresenterImp(gpsSpoofingSettingView, activityInteractor)
    }

    @Provides
    fun provideGpsSpoofingView(): GpsSpoofingSettingView {
        return gpsSpoofingSettingView
    }

    @Provides
    fun provideHelpPresenter(activityInteractor: ActivityInteractor): HelpPresenter {
        return HelpPresenterImpl(helpView, activityInteractor)
    }

    @Provides
    fun provideHelpView(): HelpView {
        return helpView
    }

    @Provides
    fun provideMainMenuView(): MainMenuView {
        return mainMenuView
    }

    @Provides
    fun provideMenuPresenter(
            activityInteractor: ActivityInteractor
    ): MainMenuPresenter {
        return MainMenuPresenterImpl(mainMenuView, activityInteractor)
    }

    @Provides
    fun provideNetworkDetailPresenter(activityInteractor: ActivityInteractor): NetworkDetailPresenter {
        return NetworkDetailPresenterImp(networkDetailView, activityInteractor)
    }

    @Provides
    fun provideAdvanceParamsPresenter(preferencesHelper: PreferencesHelper): AdvanceParamPresenter {
        return AdvanceParamsPresenterImpl(advanceParamView, preferencesHelper)
    }

    @Provides
    fun provideNetworkDetailView(): NetworkDetailView {
        return networkDetailView
    }

    @Provides
    fun provideNewsPresenter(activityInteractor: ActivityInteractor): NewsFeedPresenter {
        return NewsFeedPresenterImpl(newsFeedView, activityInteractor)
    }

    @Provides
    fun provideRobertSettingsPresenter(activityInteractor: ActivityInteractor): RobertSettingsPresenter {
        return RobertSettingsPresenterImpl(robertSettingsView, activityInteractor)
    }

    @Provides
    fun provideSecurityPresenter(activityInteractor: ActivityInteractor): NetworkSecurityPresenter {
        return NetworkSecurityPresenterImpl(networkSecurityView, activityInteractor)
    }

    @Provides
    fun provideSecurityView(): NetworkSecurityView {
        return networkSecurityView
    }

    @Provides
    fun provideSendTicketPresenter(activityInteractor: ActivityInteractor): SendTicketPresenter {
        return SendTicketPresenterImpl(sendTicketView, activityInteractor)
    }

    @Provides
    fun provideSendTicketView(): SendTicketView {
        return sendTicketView
    }

    @Named("serverListFragments")
    @Provides
    fun provideServerListFragments(): List<ServerListFragment> {
        val serverListFragments: MutableList<ServerListFragment> = ArrayList()
        for (counter in 0..4) {
            serverListFragments.add(counter, ServerListFragment.newInstance(counter))
        }
        return serverListFragments
    }

    @Provides
    fun provideSplashPresenter(activityInteractor: ActivityInteractor): SplashPresenter {
        return SplashPresenterImpl(splashView, activityInteractor)
    }

    @Provides
    fun provideSplashView(): SplashView {
        return splashView
    }

    @Provides
    fun provideSplitPresenter(
            activityInteractor: ActivityInteractor
    ): SplitTunnelingPresenter {
        return SplitTunnelingPresenterImpl(splitTunnelingView, activityInteractor)
    }

    @Provides
    fun provideSplitTunnelingView(): SplitTunnelingView {
        return splitTunnelingView
    }

    @Provides
    fun provideWelcomePresenter(activityInteractor: ActivityInteractor): WelcomePresenter {
        return WelcomePresenterImpl(welcomeView, activityInteractor)
    }

    @Provides
    fun provideWelcomeView(): WelcomeView {
        return welcomeView
    }

    @Provides
    fun provideWindscribePresenter(
            activityInteractor: ActivityInteractor,
            permissionManager: PermissionManager
    ): WindscribePresenter {
        return WindscribePresenterImpl(windscribeView, activityInteractor, permissionManager)
    }

    @Provides
    fun provideWindscribeView(): WindscribeView {
        return windscribeView
    }

    @Provides
    fun providesArgbEvaluator(): ArgbEvaluator {
        return ArgbEvaluator()
    }

    @Provides
    fun providesActivityScope(): LifecycleCoroutineScope {
        return activity.lifecycleScope
    }

    @Provides
    @PerActivity
    fun providesPermissionManager(): PermissionManager {
        return PermissionManagerImpl(activity)
    }

    @Provides
    @PerActivity
    fun provideActivityInteractor(
            activityScope: LifecycleCoroutineScope,
            coroutineScope: CoroutineScope,
            prefHelper: PreferencesHelper,
            apiCallManager: IApiCallManager,
            localDbInterface: LocalDbInterface,
            vpnConnectionStateManager: VPNConnectionStateManager,
            userRepository: UserRepository,
            networkInfoManager: NetworkInfoManager,
            locationRepository: LocationRepository,
            vpnController: WindVpnController,
            connectionDataRepository: ConnectionDataRepository,
            serverListRepository: ServerListRepository,
            staticListUpdate: StaticIpRepository,
            preferenceChangeObserver: PreferenceChangeObserver,
            notificationRepository: NotificationRepository,
            workManager: WindScribeWorkManager,
            decoyTrafficController: DecoyTrafficController,
            trafficCounter: TrafficCounter,
            autoConnectionManager: AutoConnectionManager,
            latencyRepository: LatencyRepository,
            receiptValidator: ReceiptValidator,
            firebaseManager: FirebaseManager
    ): ActivityInteractor {
        return ActivityInteractorImpl(
                activityScope,
                coroutineScope,
                prefHelper,
                apiCallManager,
                localDbInterface,
                vpnConnectionStateManager,
                userRepository,
                networkInfoManager,
                locationRepository,
                vpnController,
                connectionDataRepository,
                serverListRepository,
                staticListUpdate,
                preferenceChangeObserver,
                notificationRepository,
                workManager,
                decoyTrafficController,
                trafficCounter,
                autoConnectionManager, latencyRepository, receiptValidator,
                firebaseManager
        )
    }

    @Provides
    fun providesEmergencyConnectViewModal(
            scope: CoroutineScope,
            windVpnController: WindVpnController,
            vpnConnectionStateManager: VPNConnectionStateManager
    ): Lazy<EmergencyConnectViewModal> {
        return activity.viewModels {
            return@viewModels EmergencyConnectViewModal.provideFactory(
                    scope, windVpnController, vpnConnectionStateManager
            )
        }
    }
}