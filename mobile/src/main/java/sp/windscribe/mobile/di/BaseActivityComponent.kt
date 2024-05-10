package sp.windscribe.mobile.di

import sp.windscribe.mobile.about.AboutActivity
import sp.windscribe.mobile.account.AccountActivity
import sp.windscribe.mobile.advance.AdvanceParamsActivity
import sp.windscribe.mobile.confirmemail.ConfirmActivity
import sp.windscribe.mobile.connectionsettings.ConnectionSettingsActivity
import sp.windscribe.mobile.debug.DebugViewActivity
import sp.windscribe.mobile.email.AddEmailActivity
import sp.windscribe.mobile.fragments.ServerListFragment
import sp.windscribe.mobile.generalsettings.GeneralSettingsActivity
import sp.windscribe.mobile.gpsspoofing.GpsSpoofingSettingsActivity
import sp.windscribe.mobile.help.HelpActivity
import sp.windscribe.mobile.mainmenu.MainMenuActivity
import sp.windscribe.mobile.networksecurity.NetworkSecurityActivity
import sp.windscribe.mobile.networksecurity.networkdetails.NetworkDetailsActivity
import sp.windscribe.mobile.newsfeedactivity.NewsFeedActivity
import sp.windscribe.mobile.robert.RobertSettingsActivity
import sp.windscribe.mobile.splash.SplashActivity
import sp.windscribe.mobile.splittunneling.SplitTunnelingActivity
import sp.windscribe.mobile.ticket.SendTicketActivity
import sp.windscribe.mobile.welcome.WelcomeActivity
import sp.windscribe.mobile.windscribe.WindscribeActivity

interface BaseActivityComponent {
    fun inject(sendTicketActivity: SendTicketActivity)
    fun inject(helpActivity: HelpActivity)
    fun inject(confirmActivity: ConfirmActivity)
    fun inject(splashActivity: SplashActivity)
    fun inject(welcomeActivity: WelcomeActivity)
    fun inject(networkDetailsActivity: NetworkDetailsActivity)
    fun inject(windscribeActivity: WindscribeActivity)
    fun inject(mainMenuActivity: MainMenuActivity)
    fun inject(generalSettingsActivity: GeneralSettingsActivity)
    fun inject(networkSecurityActivity: NetworkSecurityActivity)
    fun inject(accountActivity: AccountActivity)
    fun inject(newsFeedActivity: NewsFeedActivity)
    fun inject(addEmailActivity: AddEmailActivity)
    fun inject(settingsActivity: ConnectionSettingsActivity)
    fun inject(splitTunnelingActivity: SplitTunnelingActivity)
    fun inject(serverListFragment: ServerListFragment)
    fun inject(gpsSpoofingSettingsActivity: GpsSpoofingSettingsActivity)
    fun inject(aboutActivity: AboutActivity)
    fun inject(robertSettingsActivity: RobertSettingsActivity)
    fun inject(debugViewActivity: DebugViewActivity)
    fun inject(advanceParamsActivity: AdvanceParamsActivity)
}