package sp.windscribe.mobile.robert

import sp.windscribe.mobile.adapter.RobertSettingsAdapter

interface RobertSettingsView {
    fun hideProgress()
    fun openUrl(url: String)
    fun setAdapter(robertSettingsAdapter: RobertSettingsAdapter)
    fun setTitle(title: String)
    fun setWebSessionLoading(loading: Boolean)
    fun showError(error: String)
    fun showErrorDialog(error: String)
    fun showProgress()
    fun showToast(message: String)
}