package sp.windscribe.mobile.confirmemail

interface ConfirmEmailPresenter {
    fun init(reasonToConfirmEmail: String?)
    fun onDestroy()
    fun resendVerificationEmail()
}