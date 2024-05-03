package sp.windscribe.vpn.exceptions

import sp.windscribe.vpn.repository.CallResult

class InvalidVPNConfigException(val error: CallResult.Error) : Exception() {
    override val message: String
        get() = "Code: ${error.code} error: ${error.errorMessage}"
}