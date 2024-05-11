package sp.windscribe.vpn.serverlist.sort

import sp.windscribe.vpn.serverlist.entity.StaticRegion

class ByStaticRegionName : Comparator<StaticRegion> {
    override fun compare(o1: StaticRegion, o2: StaticRegion): Int {
        return o1.cityName.compareTo(o2.cityName)
    }
}