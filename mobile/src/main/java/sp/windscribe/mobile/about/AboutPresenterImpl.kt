package sp.windscribe.mobile.about

import sp.windscribe.mobile.R
import sp.windscribe.vpn.ActivityInteractor
import sp.windscribe.vpn.constants.NetworkKeyConstants
import sp.windscribe.vpn.constants.NetworkKeyConstants.getWebsiteLink
import javax.inject.Inject

class AboutPresenterImpl @Inject constructor(
    val aboutView: AboutView,
    val interactor: ActivityInteractor
) : AboutPresenter {
    override fun init() {
        aboutView.setTitle(interactor.getResourceString(R.string.about))
    }

    override val isHapticFeedbackEnabled: Boolean
        get() = interactor.getAppPreferenceInterface().isHapticFeedbackEnabled

    override fun onAboutClick() {
        aboutView.openUrl(getWebsiteLink(NetworkKeyConstants.URL_ABOUT))
    }

    override fun onBlogClick() {
        aboutView.openUrl(NetworkKeyConstants.URL_BLOG)
    }

    override fun onJobsClick() {
        aboutView.openUrl(getWebsiteLink(NetworkKeyConstants.URL_JOB))
    }

    override fun onPrivacyClick() {
        aboutView.openUrl(getWebsiteLink(NetworkKeyConstants.URL_PRIVACY))
    }

    override fun onStatusClick() {
        aboutView.openUrl(getWebsiteLink(NetworkKeyConstants.URL_STATUS))
    }

    override fun onTermsClick() {
        aboutView.openUrl(getWebsiteLink(NetworkKeyConstants.URL_TERMS))
    }

    override fun onViewLicenceClick() {
        aboutView.openUrl(getWebsiteLink(NetworkKeyConstants.URL_VIEW_LICENCE))
    }
}