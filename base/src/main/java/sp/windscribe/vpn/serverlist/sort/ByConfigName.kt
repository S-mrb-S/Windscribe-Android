package sp.windscribe.vpn.serverlist.sort

import sp.windscribe.vpn.serverlist.entity.ConfigFile
import java.util.Comparator

class ByConfigName : Comparator<ConfigFile> {
    override fun compare(o1: ConfigFile, o2: ConfigFile): Int {
        return o1.name.compareTo(o2.name)
    }
}