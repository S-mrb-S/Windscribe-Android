package sp.windscribe.mobile.connectionui

class ConnectionOptionsBuilder {
    private var isPreferred: Boolean = false
    fun build(): ConnectionOptions {
        return ConnectionOptions(isPreferred)
    }

    fun setIsPreferred(isPreferred: Boolean): ConnectionOptionsBuilder {
        this.isPreferred = isPreferred
        return this
    }
}