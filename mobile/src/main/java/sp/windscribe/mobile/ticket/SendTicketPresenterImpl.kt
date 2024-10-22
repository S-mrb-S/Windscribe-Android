package sp.windscribe.mobile.ticket

import android.content.Context
import android.util.Patterns
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import sp.windscribe.mobile.R
import sp.windscribe.vpn.ActivityInteractor
import sp.windscribe.vpn.api.CreateHashMap.buildTicketMap
import sp.windscribe.vpn.api.response.ApiErrorResponse
import sp.windscribe.vpn.api.response.GenericResponseClass
import sp.windscribe.vpn.api.response.QueryType
import sp.windscribe.vpn.api.response.TicketResponse
import sp.windscribe.vpn.constants.NetworkErrorCodes
import sp.windscribe.vpn.constants.PreferencesKeyConstants
import sp.windscribe.vpn.repository.CallResult
import javax.inject.Inject

class SendTicketPresenterImpl @Inject constructor(
        private val sendTicketView: SendTicketView,
        private val interactor: ActivityInteractor
) : SendTicketPresenter {
    private var queryType = QueryType.Account
    override fun init() {
        sendTicketView.setActivityTitle(interactor.getResourceString(R.string.send_ticket))
        sendTicketView.setQueryTypeSpinner()
        sendTicketView.addTextChangeListener()
        interactor.getUserRepository().user.value?.email?.let {
            sendTicketView.setEmail(it)
        }
    }

    override fun onInputChanged(email: String, subject: String, message: String) {
        sendTicketView.setSendButtonState(
                validEmail(email) && validMessage(message) && validSubject(
                        subject
                )
        )
    }

    override fun onQueryTypeSelected(queryType: QueryType) {
        this.queryType = queryType
    }

    override fun onSendTicketClicked(email: String, subject: String, message: String) {
        sendTicketView.setProgressView(true)
        val username = interactor.getAppPreferenceInterface().userName
        val queryMap = buildTicketMap(email, subject, message, username, queryType)
        interactor.getCompositeDisposable()
                .add(
                        interactor.getApiCallManager().sendTicket(queryMap)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(
                                        object :
                                                DisposableSingleObserver<GenericResponseClass<TicketResponse?, ApiErrorResponse?>>() {
                                            override fun onError(e: Throwable) {
                                                sendTicketView.setProgressView(false)
                                                sendTicketView.setErrorLayout("Failed to submit ticket. Try again.")
                                            }

                                            override fun onSuccess(
                                                    response: GenericResponseClass<TicketResponse?, ApiErrorResponse?>
                                            ) {
                                                sendTicketView.setProgressView(false)
                                                when (val result = response.callResult<TicketResponse>()) {
                                                    is CallResult.Error -> {
                                                        if (result.code == NetworkErrorCodes.ERROR_UNEXPECTED_API_DATA) {
                                                            sendTicketView.setErrorLayout("Failed to submit ticket. Try again.")
                                                        } else {
                                                            sendTicketView.setErrorLayout(result.errorMessage)
                                                        }
                                                    }

                                                    is CallResult.Success -> sendTicketView.setSuccessLayout("Sweet, we’ll get back to you as soon as one of our agents is back from lunch.")
                                                }
                                            }
                                        })
                )
    }

    override fun setTheme(context: Context) {
        val savedThem = interactor.getAppPreferenceInterface().selectedTheme
        if (savedThem == PreferencesKeyConstants.DARK_THEME) {
            context.setTheme(R.style.DarkTheme)
        } else {
            context.setTheme(R.style.LightTheme)
        }
    }

    private fun validEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validMessage(message: String): Boolean {
        return message.isNotEmpty()
    }

    private fun validSubject(subject: String): Boolean {
        return subject.isNotEmpty()
    }
}