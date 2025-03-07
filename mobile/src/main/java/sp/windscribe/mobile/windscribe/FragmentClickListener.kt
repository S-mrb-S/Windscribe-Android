package sp.windscribe.mobile.windscribe

interface FragmentClickListener {
    fun onAddConfigClick()
    fun onRefreshPingsForAllServers()
    fun onRefreshPingsForConfigServers()
    fun onRefreshPingsForFavouritesServers()
    fun onRefreshPingsForStaticServers()
    fun onRefreshPingsForStreamingServers()
    fun onReloadClick()
    fun onStaticIpClick()
    fun onUpgradeClicked()
    fun setServerListToolbarElevation(elevation: Int)
}
