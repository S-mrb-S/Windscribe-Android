package sp.windscribe.mobile.sp.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Error
import com.apollographql.apollo3.api.http.HttpMethod
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.vpn.sp.Data
import sp.windscribe.vpn.sp.Static

class GetServersWithKeyQuery {

    interface GetServersCallback {
        fun onSuccess(data: GetServersQuery.Data?)
        fun onFailure(errors: List<Error>?)
    }

    suspend fun performWork(key: String, callback: GetServersCallback) {
        try {
            val apolloClient = ApolloClient.Builder()
                    .httpMethod(HttpMethod.Get)
                    .httpServerUrl(Data.static.myApi)
                    .build()

            val response = apolloClient.query(GetServersQuery(key = key))
                    .execute()

            if (!response.hasErrors()) {
                callback.onSuccess(response.data)
            } else {
                callback.onFailure(response.errors)
            }

        } catch (e: Exception) {
            callback.onFailure(null)
        }
    }

}