package sp.windscribe.mobile.newsfeedactivity

import sp.windscribe.vpn.localdatabase.tables.WindNotification

interface NewsFeedListener {
    fun onNotificationActionClick(windNotification: WindNotification)
    fun onNotificationExpand(windNotification: WindNotification)
}