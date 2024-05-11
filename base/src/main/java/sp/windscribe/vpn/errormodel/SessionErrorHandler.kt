package sp.windscribe.vpn.errormodel

import sp.windscribe.vpn.R
import sp.windscribe.vpn.Windscribe.Companion.appContext
import sp.windscribe.vpn.api.response.ApiErrorResponse
import sp.windscribe.vpn.constants.NetworkErrorCodes

class SessionErrorHandler private constructor() {
    companion object {
        @JvmStatic
        val instance = SessionErrorHandler()
    }

    fun getErrorMessage(apiErrorResponse: ApiErrorResponse): String {
        return when (apiErrorResponse.errorCode) {
            NetworkErrorCodes.ERROR_2FA_REQUIRED -> appContext.resources.getString(R.string.fa_required_error)
            NetworkErrorCodes.ERROR_INVALID_2FA -> appContext.resources.getString(R.string.fa_invalid_error)
            NetworkErrorCodes.ERROR_RESPONSE_ARGUMENT_INVALID -> appContext.resources.getString(
                R.string.password_too_short
            )

            else -> apiErrorResponse.errorMessage
        }
    }

    fun getErrorMessage(errorCode: Int, error: String): String {
        return when (errorCode) {
            NetworkErrorCodes.ERROR_2FA_REQUIRED -> appContext.resources.getString(R.string.fa_required_error)
            NetworkErrorCodes.ERROR_INVALID_2FA -> appContext.resources.getString(R.string.fa_invalid_error)
            NetworkErrorCodes.ERROR_RESPONSE_ARGUMENT_INVALID -> appContext.resources.getString(
                R.string.password_too_short
            )

            else -> error
        }
    }
}
