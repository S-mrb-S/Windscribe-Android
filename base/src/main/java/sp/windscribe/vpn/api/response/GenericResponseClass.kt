package sp.windscribe.vpn.api.response

import sp.windscribe.vpn.constants.NetworkErrorCodes
import sp.windscribe.vpn.repository.CallResult

class GenericResponseClass<D, E>(val dataClass: D?, val errorClass: E?) {

    fun <T> callResult(): CallResult<T> {
        return if (dataClass != null) {
            CallResult.Success(dataClass as T)
        } else if (errorClass != null && errorClass is ApiErrorResponse) {
            CallResult.Error(errorClass.errorCode, errorClass.errorMessage)
        } else {
            CallResult.Error(
                    NetworkErrorCodes.ERROR_UNEXPECTED_API_DATA,
                    "Unexpected Api data returned from Api."
            )
        }
    }
}