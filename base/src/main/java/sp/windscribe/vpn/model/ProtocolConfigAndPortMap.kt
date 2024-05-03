/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.model

import sp.windscribe.vpn.api.response.PortMapResponse.PortMap
import sp.windscribe.vpn.backend.utils.ProtocolConfig

class ProtocolConfigAndPortMap(val protocolConfig: ProtocolConfig, val portMap: PortMap? = null)
