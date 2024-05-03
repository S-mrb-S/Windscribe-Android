package sp.windscribe.mobile.robert

import android.content.Context

interface RobertSettingsPresenter {
    val savedLocale: String?
    fun init()
    fun onCustomRulesClick()
    fun onDestroy()
    fun onLearnMoreClick()
    fun setTheme(context: Context)
}