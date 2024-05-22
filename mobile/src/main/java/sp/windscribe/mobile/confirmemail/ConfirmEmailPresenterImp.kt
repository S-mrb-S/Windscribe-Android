package sp.windscribe.mobile.confirmemail

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import sp.windscribe.mobile.R
import sp.windscribe.vpn.ActivityInteractor
import sp.windscribe.vpn.api.response.AddEmailResponse
import sp.windscribe.vpn.api.response.ApiErrorResponse
import sp.windscribe.vpn.api.response.GenericResponseClass
import sp.windscribe.vpn.constants.UserStatusConstants
import sp.windscribe.vpn.repository.CallResult
import javax.inject.Inject

class ConfirmEmailPresenterImp @Inject constructor(
        private var confirmEmailView: ConfirmEmailView,
        private var interactor: ActivityInteractor
) : ConfirmEmailPresenter {
    private val mPresenterLog = LoggerFactory.getLogger("[confirm-email-i]")
    override fun onDestroy() {
        if (interactor.getCompositeDisposable().isDisposed.not()) {
            interactor.getCompositeDisposable().dispose()
        }
    }

    override fun init(reasonToConfirmEmail: String?) {
        reasonToConfirmEmail?.let {
            confirmEmailView.setReasonToConfirmEmail(it)
        } ?: kotlin.run {
            val proUser = (interactor.getAppPreferenceInterface().userStatus
                    == UserStatusConstants.USER_STATUS_PREMIUM)
            val reasonForConfirmEmail = interactor
                    .getResourceString(if (proUser) R.string.pro_reason_to_confirm else R.string.free_reason_to_confirm)
            confirmEmailView.setReasonToConfirmEmail(reasonForConfirmEmail)
        }
    }

    override fun resendVerificationEmail() {
        confirmEmailView.showEmailConfirmProgress(true)
        interactor.getCompositeDisposable().add(
                interactor.getApiCallManager().resendUserEmailAddress(null).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribeWith(object :
                                DisposableSingleObserver<GenericResponseClass<AddEmailResponse?, ApiErrorResponse?>>() {
                            override fun onError(e: Throwable) {
                                confirmEmailView.showToast(interactor.getResourceString(R.string.error_sending_email))
                                confirmEmailView.showEmailConfirmProgress(false)
                            }

                            override fun onSuccess(
                                    postEmailResponseClass: GenericResponseClass<AddEmailResponse?, ApiErrorResponse?>
                            ) {
                                confirmEmailView.showEmailConfirmProgress(false)
                                when (val result = postEmailResponseClass.callResult<AddEmailResponse>()) {
                                    is CallResult.Error -> {
                                        confirmEmailView.showToast(result.errorMessage)
                                        mPresenterLog.debug("Server returned error. $result")
                                    }

                                    is CallResult.Success -> {
                                        confirmEmailView.showToast(interactor.getResourceString(R.string.email_confirmation_sent_successfully))
                                        mPresenterLog.info("Email confirmation sent successfully...")
                                        confirmEmailView.finishActivity()
                                    }
                                }
                            }
                        })
        )
    }
}