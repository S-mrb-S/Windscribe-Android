package sp.windscribe.mobile.email

interface AddEmailPresenter {
    fun onAddEmailClicked(emailAddress: String)
    fun onDestroy()
    fun setUpLayout()
}