package com.akinci.socialapitrial.feature.login.data.api

import com.akinci.socialapitrial.common.network.RestConfig
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.POST

interface LoginServiceDao {
    @POST(RestConfig.REQUEST_TOKEN_URL)
    suspend fun requestToken() : Response<String>

    @POST(RestConfig.REQUEST_ACCESS_TOKEN)
    suspend fun getAccessToken(
        @Field("oauth_token") oauthToken : String,
        @Field("oauth_verifier") oauthVerifier : String
    ) : Response<String>
}