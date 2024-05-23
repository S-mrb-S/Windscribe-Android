package sp.windscribe.vpn.sp

import com.tencent.mmkv.MMKV

object Data {
    const val Api = "http://sub.iprostable.enterprises/graphql"

    var defaultItemDialog = 0 // 0 --> V2ray, 1 --> OpenVpn, 2 --> cisco

    val item_options = arrayOf("V2ray", "OpenVpn", "Cisco")

    val settingsStorage: MMKV = MmkvManager.getSettingsStorage()
    val serviceStorage: MMKV = MmkvManager.getServiceStorage()

    var dataString: String? = "" // finally servers
}// HI