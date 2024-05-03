package sp.windscribe.mobile.networksecurity.networkdetails

import sp.windscribe.vpn.localdatabase.tables.NetworkInfo

interface NetworkDetailView {
    var networkInfo: NetworkInfo?
    fun onNetworkDeleted()
    fun onNetworkDetailAvailable(networkInfo: NetworkInfo, inUse: Boolean)
    fun setAutoSecureToggle(autoSecure: Boolean)
    fun setNetworkDetailError(show: Boolean, error: String?)
    fun setPreferredProtocolToggle(preferredProtocol: Boolean)
    fun setupPortMapAdapter(port: String, portMap: List<String>)
    fun setupProtocolAdapter(protocol: String, protocols: Array<String>)
    fun showToast(message: String)
    fun setActivityTitle(title: String)
}