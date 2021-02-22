package com.akinci.socialapitrial.feature.login.data.api

import com.akinci.socialapitrial.common.network.RestConfig
import com.akinci.socialapitrial.feature.login.data.output.RequestTokenResponse
import retrofit2.Response
import retrofit2.http.POST

interface LoginServiceDao {
    @POST(RestConfig.REQUEST_TOKEN_URL)
    suspend fun requestToken() : Response<String>
}