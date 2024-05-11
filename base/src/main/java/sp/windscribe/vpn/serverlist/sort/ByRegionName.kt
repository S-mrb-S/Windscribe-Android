package sp.windscribe.vpn.serverlist.sort

import sp.windscribe.vpn.serverlist.entity.Group

class ByRegionName : Comparator<Group> {
    override fun compare(o1: Group, o2: Group): Int {
        return o1.region.name.compareTo(o2.region.name)
    }
}