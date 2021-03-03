package com.akinci.socialapitrial.feature.login.repository

import android.net.Uri
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.common.repository.BaseRepositoryImpl
import com.akinci.socialapitrial.common.storage.LocalPreferenceConfig
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.feature.login.data.api.LoginServiceDao
import com.akinci.socialapitrial.feature.login.data.output.AccessTokenResponse
import com.akinci.socialapitrial.feature.login.data.output.RequestTokenResponse
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginServiceDao,
    private val networkChecker: NetworkChecker
) : BaseRepositoryImpl(networkChecker), LoginRepository {

    override suspend fun requestToken(): Resource<RequestTokenResponse> {
        return callService(
                retrofitServiceAction = { loginService.requestToken() },
                customResponseMappingAction = {
                    val responseUrlParameters = if(it.contains("?")) { it } else { "?$it" }
                    val uri = Uri.parse(responseUrlParameters)
                    val authToken = uri.getQueryParameter("oauth_token") ?: ""
                    val authTokenSecret = uri.getQueryParameter("oauth_token_secret") ?: ""

                    if(authToken.isEmpty() || authTokenSecret.isEmpty()){
                        Resource.Error("Request URL authToken/authTokenSecret is empty")
                    }else{
                        Resource.Success(RequestTokenResponse(authToken, authTokenSecret))
                    }
                }
        )
    }

    override suspend fun getAccessToken(oauthToken: String, oauthTokenSecret: String): Resource<AccessTokenResponse> {
        return callService(
                retrofitServiceAction = { loginService.getAccessToken(oauthToken, oauthTokenSecret) },
                customResponseMappingAction = {

                    //TODO Su logic baska yere tasinabilir
                    val responseUrlParameters = if(it.contains("?")) { it } else { "?$it" }
                    val uri = Uri.parse(responseUrlParameters)
                    val accessToken = uri.getQueryParameter("oauth_token") ?: ""
                    val accessTokenSecret = uri.getQueryParameter("oauth_token_secret") ?: ""
                    val userId = uri.getQueryParameter("user_id") ?: ""
                    val screenName = uri.getQueryParameter("screen_name") ?: ""

                    if(accessToken.isEmpty() || accessTokenSecret.isEmpty()
                            ||userId.isEmpty() || screenName.isEmpty()){
                        Resource.Error("Request URL authToken/authTokenSecret/userId/screenName is empty")
                    }else{
                        Resource.Success(AccessTokenResponse(accessToken, accessTokenSecret, userId, screenName))
                    }
                }
        )
    }
}