package sp.windscribe.vpn.api

import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import sp.windscribe.vpn.api.response.ApiErrorResponse
import sp.windscribe.vpn.constants.ApiConstants.JSON_RESPONSE_KEY

/**
 * Json converter to convert api response bodies to
 * Pojo classes
 */
object JsonResponseConverter {

    @JvmStatic
    fun getErrorClass(jsonObject: JSONObject): ApiErrorResponse {
        return Gson().fromJson(jsonObject.toString(), ApiErrorResponse::class.java)
    }

    @JvmStatic
    @Throws(JSONException::class)
    fun <T> getResponseClass(mJsonObject: JSONObject, tClass: Class<T>): T {
        return Gson().fromJson(mJsonObject.getJSONObject(JSON_RESPONSE_KEY).toString(), tClass)
    }
}
