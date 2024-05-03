package sp.windscribe.mobile.di

import sp.windscribe.mobile.dialogs.ShareAppLinkDialog
import sp.windscribe.vpn.di.ApplicationComponent
import dagger.Component

@PerDialog
@Component(dependencies = [ApplicationComponent::class], modules = [DialogModule::class])
interface DialogComponent {
    fun inject(shareAppLinkDialog: ShareAppLinkDialog)
}