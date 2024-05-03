package sp.windscribe.vpn.exceptions

class TooHighLatencyError : Exception() {
    override val message: String
        get() = "Latency > 1000 retrying..."
}
