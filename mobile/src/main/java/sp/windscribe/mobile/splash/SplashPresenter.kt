package sp.windscribe.mobile.splash

import sp.windscribe.mobile.GetServersQuery

interface SplashPresenter {
    fun checkNewMigration()
    fun onDestroy()
    fun setDataAndLoad(data: GetServersQuery.Data?)
}