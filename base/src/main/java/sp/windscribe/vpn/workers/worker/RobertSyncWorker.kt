/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.workers.worker

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import io.reactivex.Single
import sp.windscribe.vpn.ServiceInteractor
import sp.windscribe.vpn.Windscribe.Companion.appContext
import javax.inject.Inject

class RobertSyncWorker(context: Context, workerParameters: WorkerParameters) :
    RxWorker(context, workerParameters) {

    @Inject
    lateinit var interactor: ServiceInteractor

    init {
        appContext.applicationComponent.inject(this)
    }

    override fun createWork(): Single<Result> {
        return interactor.apiManager.syncRobert()
            .flatMap {
                when {
                    it.dataClass != null -> {
                        return@flatMap Single.just(Result.success())
                    }

                    it.errorClass != null -> {
                        return@flatMap Single.just(Result.failure())
                    }

                    else -> {
                        return@flatMap Single.just(Result.retry())
                    }
                }
            }.onErrorReturnItem(Result.retry())
    }
}
