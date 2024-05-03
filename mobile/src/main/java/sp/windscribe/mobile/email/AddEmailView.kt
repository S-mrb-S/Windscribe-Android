package sp.windscribe.mobile.email

interface AddEmailView {
    fun gotoWindscribeActivity()
    fun hideSoftKeyboard()
    fun prepareUiForApiCallFinished()
    fun prepareUiForApiCallStart()
    fun setUpLayout(title: String)
    fun showInputError(errorText: String)
    fun showToast(toastString: String)
}