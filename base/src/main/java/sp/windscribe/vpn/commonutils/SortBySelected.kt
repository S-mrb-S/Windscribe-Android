package sp.windscribe.vpn.commonutils

import sp.windscribe.vpn.api.response.InstalledAppsData
import java.lang.Boolean
import kotlin.Comparator
import kotlin.Int

class SortBySelected : Comparator<InstalledAppsData> {
    override fun compare(o1: InstalledAppsData, o2: InstalledAppsData): Int {
        return Boolean.compare(o2.isChecked, o1.isChecked)
    }
}