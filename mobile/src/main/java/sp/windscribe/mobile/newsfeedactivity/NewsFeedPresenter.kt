package sp.windscribe.mobile.newsfeedactivity

interface NewsFeedPresenter {
    fun init(showPopUp: Boolean, popUpId: Int)
    fun onDestroy()
}