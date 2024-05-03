package sp.windscribe.vpn.exceptions

open class WindScribeException(override val message: String?) : Exception() {
    override fun getLocalizedMessage(): String {
        return message ?: ""
    }
}
