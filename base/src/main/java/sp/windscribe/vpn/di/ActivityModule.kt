package sp.windscribe.vpn.di

import android.app.Activity
import dagger.Module

@Module
class ActivityModule {
    var mActivity: Activity? = null

    constructor()
    constructor(activity: Activity?) {
        mActivity = activity
    }
}
