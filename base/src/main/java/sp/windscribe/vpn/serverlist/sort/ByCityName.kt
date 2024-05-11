package sp.windscribe.vpn.serverlist.sort

import sp.windscribe.vpn.serverlist.entity.City

class ByCityName : Comparator<City> {
    override fun compare(o1: City, o2: City): Int {
        return (o1.nodeName + o1.nickName).compareTo(o2.nodeName + o2.nickName)
    }
}