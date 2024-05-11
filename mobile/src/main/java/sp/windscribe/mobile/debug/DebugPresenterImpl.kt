package sp.windscribe.mobile.debug

import sp.windscribe.mobile.adapter.LogViewAdapter
import sp.windscribe.vpn.ActivityInteractor

class DebugPresenterImpl(val view: DebugView, val activityInteractor: ActivityInteractor) :
    DebugPresenter {

    override suspend fun init() {
        view.showProgress(true)
        val logs = activityInteractor.getPartialLog().takeLast(350)
        val logViewAdapter = LogViewAdapter(logs)
        view.setAdapter(logViewAdapter)
        view.showProgress(false)
    }
}