package sp.windscribe.vpn.sp

import android.app.Application
import android.content.xversion.api.my.xapi
import androidx.annotation.WorkerThread
import androidx.core.content.ContextCompat
import sp.windscribe.vpn.model.MainViewModel

open class GlobalHelper : xapi() {
    @JvmField
    protected var mViewModel: MainViewModel? = null
    //    protected MmkvManager mmkvStorage;
    @JvmField
    protected var mainApplication: Application? = null
    //    protected boolean isImportantErrorBoolean = false;

    @WorkerThread
    fun MainApplicationExecuter(run: Runnable, application: Application) {
        ContextCompat.getMainExecutor(application).execute {
            run.run()
        }
    }
    @WorkerThread
    fun MainApplicationExecuter(run: () -> Unit, application: Application) {
        ContextCompat.getMainExecutor(application).execute {
            run()
        }
    }
}
