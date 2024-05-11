package sp.windscribe.mobile.di

import dagger.Component
import sp.windscribe.mobile.dialogs.ShareAppLinkDialog
import sp.windscribe.vpn.di.ApplicationComponent

@PerDialog
@Component(dependencies = [ApplicationComponent::class], modules = [DialogModule::class])
interface DialogComponent {
    fun inject(shareAppLinkDialog: ShareAppLinkDialog)
}