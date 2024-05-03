package sp.windscribe.mobile.ui

import sp.windscribe.mobile.ui.util.MmkvManager


object Data {
    const val Api = "http://panel.proservers.ir/graphql"

    var defaultItemDialog = 0 // 0 --> V2ray, 1 --> OpenVpn, 2 --> cisco
    var defaultVpnConnect = 0

    val item_options = arrayOf("V2ray", "OpenVpn", "Cisco")

    var settingsStorage = MmkvManager.getSettingsStorage()
}