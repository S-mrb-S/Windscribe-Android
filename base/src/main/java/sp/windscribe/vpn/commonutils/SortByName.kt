package sp.windscribe.vpn.commonutils

import sp.windscribe.vpn.api.response.InstalledAppsData

class SortByName : Comparator<InstalledAppsData> {
    override fun compare(o1: InstalledAppsData, o2: InstalledAppsData): Int {
        return o1.appName.compareTo(o2.appName, ignoreCase = true)
    }
}