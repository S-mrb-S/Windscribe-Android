package sp.windscribe.vpn.serverlist.interfaces

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import sp.windscribe.vpn.serverlist.entity.ConfigFile

interface ListViewClickListener {
    fun addToFavourite(cityId: Int, position: Int, adapter: RecyclerView.Adapter<ViewHolder>)
    fun deleteConfigFile(configFile: ConfigFile)
    fun editConfigFile(file: ConfigFile)
    fun onCityClick(cityId: Int)
    fun onConfigFileClicked(configFile: ConfigFile)
    fun onStaticIpClick(staticIpId: Int)
    fun onUnavailableRegion()
    fun removeFromFavourite(cityId: Int, position: Int, adapter: RecyclerView.Adapter<ViewHolder>)
    fun setScrollTo(scrollTo: Int)
}