package sp.windscribe.vpn.commonutils

import android.content.Context
import android.util.TypedValue
import androidx.work.ListenableWorker
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.rx2.await
import kotlinx.coroutines.withContext
import sp.windscribe.vpn.api.response.GenericResponseClass
import sp.windscribe.vpn.constants.NetworkErrorCodes
import sp.windscribe.vpn.decoytraffic.FakeTrafficVolume
import sp.windscribe.vpn.errormodel.WindError
import sp.windscribe.vpn.repository.CallResult

object Ext {
    suspend fun Completable.result(callback: (successful: Boolean, error: String?) -> Unit): ListenableWorker.Result {
        return try {
            await()
            callback(true, null)
            ListenableWorker.Result.success()
        } catch (e: Exception) {
            callback(false, WindError.instance.rxErrorToString(e))
            ListenableWorker.Result.failure()
        }
    }

    suspend fun <T> Single<*>.result(): CallResult<T> {
        return try {
            val response = await() as GenericResponseClass<*, *>
            response.callResult()
        } catch (e: Exception) {
            CallResult.Error(
                    NetworkErrorCodes.ERROR_UNABLE_TO_REACH_API, WindError.instance.rxErrorToString(e)
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <T> Single<T>.toResult(): Result<T> {
        return runCatching {
            withContext(Dispatchers.IO) {
                await() as T
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun Completable.toResult(): Result<*> {
        return runCatching {
            withContext(Dispatchers.IO) {
                await()
            }
        }
    }

    suspend fun Completable.result(): Boolean {
        return try {
            await()
            return true
        } catch (e: Exception) {
            false
        }
    }

    fun getFakeTrafficVolumeOptions(): Array<String> {
        return FakeTrafficVolume.values().map {
            it.name
        }.toTypedArray()
    }

    fun CoroutineScope.launchPeriodicAsync(
            repeatMillis: Long,
            action: () -> Unit
    ) = this.async {
        if (repeatMillis > 0) {
            while (isActive) {
                action()
                delay(repeatMillis)
            }
        } else {
            action()
        }
    }

    fun Context.toPx(dp: Float): Float =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
}
