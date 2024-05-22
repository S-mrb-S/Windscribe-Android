package sp.windscribe.vpn.qq

object Data {
    const val Api = "http://sub.iprostable.enterprises/graphql"

    var defaultItemDialog = 0 // 0 --> V2ray, 1 --> OpenVpn, 2 --> cisco

    val item_options = arrayOf("V2ray", "OpenVpn", "Cisco")

    var settingsStorage = MmkvManager.getSettingsStorage()

    var dataString: String? = "" // finally servers
}// HI