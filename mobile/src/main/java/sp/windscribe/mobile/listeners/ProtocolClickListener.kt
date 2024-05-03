/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.mobile.listeners

import sp.windscribe.vpn.backend.utils.ProtocolConfig

interface ProtocolClickListener {

    fun onProtocolSelected(protocolConfig: ProtocolConfig?)
}
