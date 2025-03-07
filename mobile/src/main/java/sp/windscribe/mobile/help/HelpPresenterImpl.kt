package sp.windscribe.mobile.help

import android.content.Context
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import sp.windscribe.mobile.R
import sp.windscribe.vpn.ActivityInteractor
import sp.windscribe.vpn.api.response.ApiErrorResponse
import sp.windscribe.vpn.api.response.GenericResponseClass
import sp.windscribe.vpn.api.response.GenericSuccess
import sp.windscribe.vpn.constants.NetworkKeyConstants
import sp.windscribe.vpn.constants.NetworkKeyConstants.getWebsiteLink
import sp.windscribe.vpn.constants.PreferencesKeyConstants
import sp.windscribe.vpn.constants.UserStatusConstants
import sp.windscribe.vpn.errormodel.WindError.Companion.instance
import javax.inject.Inject

class HelpPresenterImpl @Inject constructor(
        private val helpView: HelpView,
        private val interactor: ActivityInteractor
) : HelpPresenter {
    private val logger = LoggerFactory.getLogger("help_p")
    override fun init() {
        helpView.setActivityTitle(interactor.getResourceString(R.string.help_me))
    }

    override fun onDiscordClick() {
        helpView.openInBrowser(NetworkKeyConstants.URL_DISCORD)
    }

    override fun onGarryClick() {
        helpView.openInBrowser(getWebsiteLink(NetworkKeyConstants.URL_GARRY))
    }

    override fun onKnowledgeBaseClick() {
        helpView.openInBrowser(getWebsiteLink(NetworkKeyConstants.URL_KNOWLEDGE))
    }

    override fun onRedditClick() {
        helpView.openInBrowser(NetworkKeyConstants.URL_REDDIT)
    }

    override fun onSendDebugClicked() {
        val userInGhostMode = interactor.getAppPreferenceInterface().userName == "na"
        if (userInGhostMode) {
            helpView.showToast("Log in send logs.")
            return
        }
        helpView.showProgress(inProgress = true, success = false)
        logger.info("Preparing debug file...")
        val logMap: MutableMap<String, String> = HashMap()
        logMap[UserStatusConstants.CURRENT_USER_NAME] = interactor.getAppPreferenceInterface()
                .userName
        interactor.getCompositeDisposable().add(
                Single.fromCallable { interactor.getEncodedLog() }
                        .flatMap { encodedLog: String ->
                            logger.info("Reading log file successful, submitting app log...")
                            //Add log file and user name
                            logMap[NetworkKeyConstants.POST_LOG_FILE_KEY] = encodedLog
                            interactor.getApiCallManager()
                                    .postDebugLog(logMap)
                        }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                object :
                                        DisposableSingleObserver<GenericResponseClass<GenericSuccess?, ApiErrorResponse?>>() {
                                    override fun onError(e: Throwable) {
                                        helpView.showProgress(inProgress = false, success = false)
                                        if (e is Exception) {
                                            logger.debug(
                                                    "Error Submitting Log: "
                                                            + instance.rxErrorToString(e)
                                            )
                                        }
                                    }

                                    override fun onSuccess(
                                            appLogSubmissionResponse: GenericResponseClass<GenericSuccess?, ApiErrorResponse?>
                                    ) {
                                        helpView.showProgress(
                                                false,
                                                appLogSubmissionResponse.dataClass != null && appLogSubmissionResponse.dataClass?.isSuccessful == true
                                        )
                                    }
                                })
        )
    }

    override fun onSendTicketClick() {
        helpView.goToSendTicket()
    }

    override fun setTheme(context: Context) {
        val savedThem = interactor.getAppPreferenceInterface().selectedTheme
        if (savedThem == PreferencesKeyConstants.DARK_THEME) {
            context.setTheme(R.style.DarkTheme)
        } else {
            context.setTheme(R.style.LightTheme)
        }
    }
}