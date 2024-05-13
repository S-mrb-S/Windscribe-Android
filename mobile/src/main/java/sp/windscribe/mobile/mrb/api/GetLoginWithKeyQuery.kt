package sp.windscribe.mobile.mrb.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Error
import com.apollographql.apollo3.api.http.HttpMethod
import sp.windscribe.mobile.GetLoginQuery
import sp.windscribe.vpn.qq.Data
import sp.windscribe.vpn.qq.MmkvManager

class GetLoginWithKeyQuery {

    interface GetLoginCallback {
        fun onSuccess(data: GetLoginQuery.Data?)
        fun onFailure(errors: List<Error>?)
    }

    suspend fun performWork(key: String, callback: GetLoginCallback) {
        try {
            val apolloClient = ApolloClient.Builder()
                .httpMethod(HttpMethod.Get)
                .httpServerUrl(Data.Api)
                .build()

            val response = apolloClient.query(GetLoginQuery(key = key))
                .execute()

            if (!response.hasErrors()) {
                MmkvManager.getLoginStorage().encode("key_login", key)
                callback.onSuccess(response.data)
            } else {
                callback.onFailure(response.errors)
            }

        } catch (e: Exception) {
            callback.onFailure(null)
        }
    }

}