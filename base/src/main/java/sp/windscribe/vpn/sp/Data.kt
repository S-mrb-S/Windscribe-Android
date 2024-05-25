package sp.windscribe.vpn.sp

import com.tencent.mmkv.MMKV

object Data {
    var defaultItemDialog = 0 // 0 --> V2ray, 1 --> OpenVpn, 2 --> cisco

    val item_options = arrayOf("V2ray", "OpenVpn", "Cisco")

    val settingsStorage: MMKV = MmkvManager.getSettingsStorage()
    val serviceStorage: MMKV = MmkvManager.getServiceStorage()
    val static = Static.getGlobalData()

    var dataString: String? = "" // finally servers
    var lineBusy: Boolean = false
}// HI