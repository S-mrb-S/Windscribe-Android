/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import sp.windscribe.vpn.localdatabase.tables.WindNotification

data class NewsFeedNotification(
        @SerializedName("notifications")
        @Expose
        val notifications: List<WindNotification>?
)
