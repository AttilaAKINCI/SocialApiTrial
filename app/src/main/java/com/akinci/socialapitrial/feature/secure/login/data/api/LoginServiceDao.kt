package com.akinci.socialapitrial.feature.secure.login.data.api

import com.akinci.socialapitrial.common.network.RestConfig
import com.akinci.socialapitrial.feature.secure.login.data.output.SignOutResponse
import retrofit2.Response
import retrofit2.http.POST

interface LoginServiceDao {
    @POST(RestConfig.REQUEST_SIGN_OUT)
    suspend fun signOut() : Response<SignOutResponse>
}