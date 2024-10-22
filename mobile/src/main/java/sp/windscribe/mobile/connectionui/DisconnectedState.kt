package sp.windscribe.mobile.connectionui

import android.content.Context
import android.graphics.drawable.Drawable
import sp.windscribe.mobile.R
import sp.windscribe.vpn.backend.utils.LastSelectedLocation

class DisconnectedState(
        lastSelectedLocation: LastSelectedLocation,
        connectionOptions: ConnectionOptions,
        context: Context
) : ConnectionUiState(lastSelectedLocation, connectionOptions, context) {

    override val connectionStatusBackground: Drawable?
        get() = getDrawable(R.drawable.ic_disconnected_status_bg)

    override val flagGradientEndColor: Int
        get() = if (isCustomBackgroundEnabled) {
            getColorResource(R.color.colorDeepBlue50)
        } else {
            getColorResource(R.color.colorDeepBlue0)
        }

    override val flagGradientStartColor: Int
        get() = if (isCustomBackgroundEnabled) {
            getColorResource(R.color.colorDeepBlue50)
        } else {
            getColorResource(R.color.colorDeepBlue0)
        }

    override val preferredProtocolStatusDrawable: Drawable?
        get() {
            return if (connectionOptions.isPreferred) {
                getDrawable(R.drawable.ic_preferred_protocol_status_disabled)
            } else {
                null
            }
        }
}