package sp.windscribe.mobile.networksecurity

import sp.windscribe.vpn.localdatabase.tables.NetworkInfo

interface NetworkSecurityView {
    fun hideProgress()
    fun setupCurrentNetwork(networkInfo: NetworkInfo)
    fun onAdapterLoadFailed(showUpdate: String)
    fun openNetworkSecurityDetails(networkName: String)
    fun setAdapter(mNetworkList: List<NetworkInfo>?)
    fun showProgress(progressTitle: String)
    fun setAutoSecureToggle(resourceId: Int)
    fun hideCurrentNetwork()
}