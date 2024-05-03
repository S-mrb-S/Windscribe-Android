package sp.windscribe.mobile.welcome.fragment

interface WelcomeActivityCallback {
    fun clearInputErrors()
    fun setLoginError(error: String)
    fun setPasswordError(error: String)
    fun setUsernameError(error: String)
}