package sp.windscribe.vpn.qq

import sp.windscribe.vpn.serverlist.entity.City
import sp.windscribe.vpn.serverlist.entity.Region
import sp.windscribe.vpn.serverlist.entity.RegionAndCities


object Data {
    const val Api = "http://panel.proservers.ir/graphql"

    var defaultItemDialog = 0 // 0 --> V2ray, 1 --> OpenVpn, 2 --> cisco
    var defaultVpnConnect = 0

    val item_options = arrayOf("V2ray", "OpenVpn", "Cisco")

    var settingsStorage = MmkvManager.getSettingsStorage()

    var staticRegion: List<RegionAndCities>? = null
    var cities: List<City> = ArrayList()
    var regsion: List<Region> = ArrayList()

    var dataString = ""
}// HI