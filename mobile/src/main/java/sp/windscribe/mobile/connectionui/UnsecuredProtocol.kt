package sp.windscribe.mobile.connectionui

import android.content.Context
import android.graphics.drawable.Drawable
import sp.windscribe.mobile.R
import sp.windscribe.vpn.backend.utils.LastSelectedLocation

class UnsecuredProtocol(
        lastSelectedLocation: LastSelectedLocation,
        connectionOptions: ConnectionOptions,
        context: Context
) : FailedProtocol(lastSelectedLocation, connectionOptions, context) {

    override val connectionStatusIcon: Drawable?
        get() = getDrawable(R.drawable.ic_wifi_unsecure_yellow)
}