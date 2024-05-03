package sp.windscribe.mobile.newsfeedactivity

import sp.windscribe.mobile.adapter.NewsFeedAdapter
import sp.windscribe.vpn.api.response.PushNotificationAction

interface NewsFeedView {
    fun hideProgress()
    fun setNewsFeedAdapter(mAdapter: NewsFeedAdapter)
    fun showLoadingError(errorMessage: String)
    fun showProgress(progressTitle: String)
    fun startUpgradeActivity(pushNotificationAction: PushNotificationAction)
}