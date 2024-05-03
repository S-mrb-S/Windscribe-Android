package sp.windscribe.mobile.confirmemail

interface ConfirmEmailView {
    fun finishActivity()
    fun setReasonToConfirmEmail(reasonForConfirmEmail: String)
    fun showEmailConfirmProgress(show: Boolean)
    fun showToast(toast: String)
}