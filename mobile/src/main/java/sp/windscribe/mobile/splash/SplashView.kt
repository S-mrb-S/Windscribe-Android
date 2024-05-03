package sp.windscribe.mobile.splash

interface SplashView {
    val isConnectedToNetwork: Boolean
    fun navigateToAccountSetUp()
    fun navigateToHome()
    fun navigateToLogin()
}