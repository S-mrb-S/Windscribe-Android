package sp.windscribe.mobile.help

import android.content.Context

interface HelpPresenter {
    fun init()
    fun onDiscordClick()
    fun onGarryClick()
    fun onKnowledgeBaseClick()
    fun onRedditClick()
    fun onSendDebugClicked()
    fun onSendTicketClick()
    fun setTheme(context: Context)
}