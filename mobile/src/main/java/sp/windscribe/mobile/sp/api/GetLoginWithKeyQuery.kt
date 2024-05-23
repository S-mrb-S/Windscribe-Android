package sp.windscribe.mobile.sp.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Error
import com.apollographql.apollo3.api.http.HttpMethod
import sp.windscribe.mobile.GetLoginQuery
import sp.windscribe.vpn.sp.Data
import sp.windscribe.vpn.sp.Static

// service
class GetLoginWithKeyQuery {

    interface GetLoginCallback {
        fun onSuccess(data: GetLoginQuery.Data?)
        fun onFailure(errors: List<Error>?)
    }

    suspend fun performWork(key: String, callback: GetLoginCallback) {
        try {
            val apolloClient = ApolloClient.Builder()
                    .httpMethod(HttpMethod.Get)
                    .httpServerUrl(Data.static.myApi)
                    .build()

            val response = apolloClient.query(GetLoginQuery(key = key))
                    .execute()

            fun success() {
                callback.onSuccess(response.data)
            }

            val name = response.data?.service?.name
            if (!response.hasErrors()) {
                success()
            } else {
                // skip response error
                if (name == null) {
                    callback.onFailure(response.errors)
                } else {
                    success()
                }
            }

        } catch (e: Exception) {
            callback.onFailure(null)
        }
    }

}