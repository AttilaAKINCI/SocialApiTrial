package com.akinci.socialapitrial.feature.login.repository

import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.login.data.output.AccessTokenResponse
import com.akinci.socialapitrial.feature.login.data.output.RequestTokenResponse

interface LoginRepository {
    suspend fun requestToken() : Resource<RequestTokenResponse>
    suspend fun getAccessToken(
        oauthToken: String,
        oauthTokenSecret: String
    ): Resource<AccessTokenResponse>
}