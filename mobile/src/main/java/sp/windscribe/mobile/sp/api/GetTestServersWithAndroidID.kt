package sp.windscribe.mobile.sp.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Error
import com.apollographql.apollo3.api.http.HttpMethod
import sp.windscribe.mobile.GetTestServerMutation
import sp.windscribe.vpn.sp.Data

class GetTestServersWithAndroidID {
        interface GetLoginCallback {
            fun onSuccess(res: GetTestServerMutation.Data?)
            fun onFailure(errors: List<Error>?)
        }

        suspend fun performWork(id: String, email: String, callback: GetLoginCallback) {
            try {
                val apolloClient = ApolloClient.Builder()
                    .serverUrl(Data.static.myApi)
                    .build()

                val response = apolloClient.mutation(GetTestServerMutation(deviceId = id, email = email))
                    .execute()

                fun success() {
                    callback.onSuccess(response.data)
                }

                val name = response.data?.createService?.service
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