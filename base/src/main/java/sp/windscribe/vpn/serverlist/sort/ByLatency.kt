package sp.windscribe.vpn.serverlist.sort

import sp.windscribe.vpn.serverlist.entity.Group

class ByLatency : Comparator<Group> {
    override fun compare(o1: Group, o2: Group): Int {
        return o1.latencyAverage - o2.latencyAverage
    }
}