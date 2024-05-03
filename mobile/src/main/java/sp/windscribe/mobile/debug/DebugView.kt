package sp.windscribe.mobile.debug

import sp.windscribe.mobile.adapter.LogViewAdapter

interface DebugView {
    fun showProgress(show: Boolean)
    fun setAdapter(logViewAdapter: LogViewAdapter)
}