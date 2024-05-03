package sp.windscribe.vpn.exceptions

import sp.windscribe.vpn.api.response.ApiErrorResponse

class GenericApiException(message: String) : Exception(message) {
    constructor(apiErrorResponse: ApiErrorResponse?) : this(apiErrorResponse.toString())
}