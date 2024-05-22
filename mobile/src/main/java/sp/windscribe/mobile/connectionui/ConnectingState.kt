package sp.windscribe.mobile.connectionui

import android.content.Context
import sp.windscribe.vpn.backend.utils.LastSelectedLocation

class ConnectingState(
        lastSelectedLocation: LastSelectedLocation,
        connectionOptions: ConnectionOptions,
        context: Context
) : ConnectingAnimationState(lastSelectedLocation, connectionOptions, context)