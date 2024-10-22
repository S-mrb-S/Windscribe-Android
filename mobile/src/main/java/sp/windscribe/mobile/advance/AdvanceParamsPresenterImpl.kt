package sp.windscribe.mobile.advance

import sp.windscribe.vpn.apppreference.PreferencesHelper
import javax.inject.Inject

class AdvanceParamsPresenterImpl @Inject constructor(
        private var advanceParamsView: AdvanceParamView,
        private val preferencesHelper: PreferencesHelper
) : AdvanceParamPresenter {
    override fun setup() {
        advanceParamsView.setupActivityTitle()
        advanceParamsView.setAdvanceParamsText(preferencesHelper.advanceParamText)
    }

    override fun saveAdvanceParams(text: String) {
        val lineCount = text.split("\n").count { it.isNotEmpty() }
        if (lineCount == 0 || text.isEmpty()) {
            advanceParamsView.showToast("Nothing to save!! Please add at least 1 key=value pair.")
            return
        }
        val lines = text.split("\n").filter { it.isNotEmpty() }
        val invalidLines = lines.filter {
            val kv = it.split("=")
            kv.count() != 2 || (kv.count() == 2 && (kv[0].isEmpty() || kv[1].isEmpty()))
        }
        if (invalidLines.isNotEmpty()) {
            val error = invalidLines.joinToString(prefix = "Invalid key/value: ", separator = ",")
            advanceParamsView.showToast(error)
            return
        }
        preferencesHelper.advanceParamText = text
        advanceParamsView.showToast("Saved successfully.")
    }

    override fun clearAdvanceParams() {
        preferencesHelper.advanceParamText = ""
        advanceParamsView.setAdvanceParamsText("")
    }
}