package sp.windscribe.mobile.welcome.fragment

interface FragmentCallback {
    fun onAccountClaimButtonClick(
            username: String,
            password: String,
            email: String,
            ignoreEmptyEmail: Boolean
    )

    fun onBackButtonPressed()
    fun onContinueWithOutAccountClick()
    fun onForgotPasswordClick()
    fun onLoginButtonClick(username: String, password: String, twoFa: String)
    fun onLoginClick()
    fun onEmergencyClick()
    fun onSignUpButtonClick(
            username: String,
            password: String,
            email: String,
            referralUsername: String,
            ignoreEmptyEmail: Boolean
    )

    fun onSkipToHomeClick()
}