package sp.windscribe.mobile.mainmenu

import android.content.Context
import android.view.View
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import sp.windscribe.mobile.R
import sp.windscribe.mobile.sp.util.list.mgToGb
import sp.windscribe.mobile.utils.UiUtil.getDataRemainingColor
import sp.windscribe.vpn.ActivityInteractor
import sp.windscribe.vpn.api.response.ApiErrorResponse
import sp.windscribe.vpn.api.response.GenericResponseClass
import sp.windscribe.vpn.api.response.UserSessionResponse
import sp.windscribe.vpn.constants.PreferencesKeyConstants
import sp.windscribe.vpn.errormodel.WindError.Companion.instance
import sp.windscribe.vpn.model.User
import sp.windscribe.vpn.sp.Data
import java.text.DecimalFormat
import javax.inject.Inject

class MainMenuPresenterImpl @Inject constructor(
        private var mainMenuView: MainMenuView,
        private var interactor: ActivityInteractor
) : MainMenuPresenter {
    private val mPresenterLog = LoggerFactory.getLogger(TAG)
    override fun onDestroy() {
        // Dispose any composite disposable
        interactor.getCompositeDisposable()
        if (!interactor.getCompositeDisposable().isDisposed) {
            mPresenterLog.info("Disposing observer...")
            interactor.getCompositeDisposable().dispose()
        }
    }

    override fun continueWithLogoutClicked() {
        interactor.getMainScope().launch {
            interactor.getUserRepository().logout()
        }
    }

    override fun savedLocale(): String {
        val selectedLanguage = interactor.getAppPreferenceInterface().savedLanguage
        return selectedLanguage
                .substring(selectedLanguage.indexOf("(") + 1, selectedLanguage.indexOf(")"))
    }

    override fun onAboutClicked() {
        mainMenuView.gotoAboutActivity()
    }

    override fun cleanDataBase() {
        interactor.getCompositeDisposable().add(
            interactor.getServerListUpdater().cleanDatabase()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                    }
                })
        )
    }

    override fun onAccountSetUpClicked() {
        mainMenuView.startAccountSetUpActivity()
    }

    override fun onAddEmailClicked() {
        mainMenuView.startAddEmailActivity()
    }

    override fun onConfirmEmailClicked() {
        mainMenuView.openConfirmEmailActivity()
    }

    override fun onConnectionSettingsClicked() {
        mPresenterLog.info("Starting settings activity...")
        mainMenuView.gotoConnectionSettingsActivity()
    }

    override fun onGeneralSettingsClicked() {
        mPresenterLog.info("Going to general settings activity...")
        mainMenuView.gotoGeneralSettingsActivity()
    }

    override fun onHelpMeClicked() {
        mPresenterLog.info("User clicked on Help..")
        mainMenuView.gotToHelp()
    }

    override fun onLanguageChanged() {
        mainMenuView.resetAllTextResources(
                interactor.getResourceString(R.string.preference),
                interactor.getResourceString(R.string.general),
                interactor.getResourceString(R.string.my_account),
                interactor.getResourceString(R.string.connection),
                interactor.getResourceString(R.string.help_me),
                interactor.getResourceString(R.string.logout),
                interactor.getResourceString(R.string.about),
                interactor.getResourceString(R.string.robert)
        )
    }

    override fun isHapticFeedbackEnabled(): Boolean {
        return interactor.getAppPreferenceInterface().isHapticFeedbackEnabled
    }

    override fun onLoginClicked() {
        mainMenuView.startLoginActivity()
    }

    override fun onMyAccountClicked() {
        mPresenterLog.info("Going to account activity...")
        mainMenuView.gotoAccountActivity()
    }

    override fun onRobertSettingsClicked() {
        mainMenuView.goRobertSettingsActivity()
    }

    override fun onSignOutClicked() {
        mainMenuView.showLogoutAlert()
    }

    override fun onReferForDataClick() {
        mainMenuView.showShareLinkDialog()
    }

    override fun onUpgradeClicked() {
        mPresenterLog.info("Showing upgrade dialog to the user...")
        mainMenuView.openUpgradeActivity()
    }

    override fun setLayoutFromApiSession() {
        interactor.getCompositeDisposable().add(
                interactor.getApiCallManager()
                        .getSessionGeneric(null)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                object :
                                        DisposableSingleObserver<GenericResponseClass<UserSessionResponse?, ApiErrorResponse?>?>() {
                                    override fun onError(e: Throwable) {
                                        mPresenterLog.debug(
                                                "Error while making get session call:" +
                                                        instance.convertThrowableToString(e)
                                        )
                                    }

                                    override fun onSuccess(response: GenericResponseClass<UserSessionResponse?, ApiErrorResponse?>) {
                                        if (response.dataClass != null) {
                                            interactor.getUserRepository().reload(response.dataClass)
                                        } else if (response.errorClass != null) {
                                            mPresenterLog
                                                    .debug(
                                                            "Server returned error during get session call." +
                                                                    response.errorClass.toString()
                                                    )
                                        }
                                    }
                                })
        )
    }

    override fun observeUserChange(mainMenuActivity: MainMenuActivity) {
        mainMenuView.setActivityTitle(interactor.getResourceString(R.string.preferences))

        Data.static.getmViewModel().dataLeft.observe(mainMenuActivity) { ddl ->
            mainMenuView.setupLayoutForFreeUser(
                    mgToGb(ddl),
                    interactor.getResourceString(R.string.get_more_data),
                    getDataRemainingColor(ddl.toFloat(), Data.serviceStorage.getInt(
                            "dailyQuota_service", // مقدار حجم کل محدودیت روزانه به مگ
                            0
                    ).toLong())
            )
        }
//        interactor.getUserRepository().user.observe(mainMenuActivity) { user ->
//            setLayoutFromUserSession(user)
//        }
    }

    override fun setTheme(context: Context) {
        val savedThem = interactor.getAppPreferenceInterface().selectedTheme
        mPresenterLog.debug("Setting theme to $savedThem")
        if (savedThem == PreferencesKeyConstants.DARK_THEME) {
            context.setTheme(R.style.DarkTheme)
        } else {
            context.setTheme(R.style.LightTheme)
        }
    }

    private fun setupActionButton(user: User) {
        if (user.isPro && user.isGhost) {
            mainMenuView.setLoginButtonVisibility(View.GONE)
            mainMenuView.setActionButtonVisibility(View.GONE, View.GONE, View.VISIBLE, View.GONE)
        } else if (user.isGhost) {
            mainMenuView.setLoginButtonVisibility(View.GONE)
            mainMenuView.setActionButtonVisibility(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE)
        } else if (user.emailStatus === User.EmailStatus.NoEmail) {
            mainMenuView.setLoginButtonVisibility(View.VISIBLE)
            mainMenuView.setActionButtonVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE)
        } else if (user.emailStatus === User.EmailStatus.EmailProvided) {
            mainMenuView.setLoginButtonVisibility(View.VISIBLE)
            mainMenuView.setActionButtonVisibility(View.GONE, View.GONE, View.GONE, View.VISIBLE)
        } else {
            mainMenuView.setLoginButtonVisibility(View.VISIBLE)
            mainMenuView.setActionButtonVisibility(View.GONE, View.GONE, View.GONE, View.GONE)
        }
    }

    private fun setLayoutFromUserSession(user: User) {
        if (user.maxData != -1L) {
            user.dataLeft?.let {
                val dataRemaining = interactor.getDataLeftString(R.string.data_left, it)
                mainMenuView.setupLayoutForFreeUser(
                        dataRemaining,
                        interactor.getResourceString(R.string.get_more_data),
                        getDataRemainingColor(it, user.maxData)
                )
            }
        } else {
            mainMenuView.setupLayoutForPremiumUser()
        }
        if (user.isGhost.not() && user.isPro.not()) {
            mainMenuView.showShareLinkOption()
        }
        setupActionButton(user)
    }

    override fun advanceViewClick() {
        mainMenuView.showAdvanceParamsActivity()
    }

    companion object {
        private const val TAG = "main_menu_p"
    }
}