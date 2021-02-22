package com.akinci.socialapitrial.feature.login.repository

import android.net.Uri
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.network.NetworkChecker
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
    private val networkChecker: NetworkChecker,
    private val sharedPreferences: Preferences
) : LoginRepository {

    override suspend fun requestToken(): Resource<RequestTokenResponse> {
        return try {
            if (networkChecker.isNetworkConnected()) {
                //internet connection is established.

                val response = loginService.requestToken()
                if (response.isSuccessful) {
                    /** 200 -> 299 Error status range **/
                    response.body()?.let {

                        val uri = Uri.parse(it)
                        val authToken = uri.getQueryParameter("oauth_token") ?: ""
                        val authTokenSecret = uri.getQueryParameter("oauth_token_secret") ?: ""

                        return@let Resource.Success(RequestTokenResponse(authToken, authTokenSecret))
                    } ?: Resource.Error("Login service response body is null")
                }else{
                    /** 400 -> 599 Error status range **/
                    Timber.d("Response failed: %s", response.body().toString())
                    Resource.Error("Response failed with code: " + response.code())
                }
            }else{
                // not connected to internet
                Resource.Error("Couldn't reached to server. Please check your internet connection")
            }
        }catch (ex : Exception){
            Timber.d(ex)
            Resource.Error("requestToken Service connection error.")
        }
    }

    override suspend fun getAccessToken(oauthToken: String, oauthTokenSecret: String): Resource<AccessTokenResponse> {
        return try {
            if (networkChecker.isNetworkConnected()) {
                //internet connection is established.

                val response = loginService.getAccessToken(oauthToken, oauthTokenSecret)
                if (response.isSuccessful) {
                    /** 200 -> 299 Error status range **/
                    response.body()?.let {
                        val uri = Uri.parse(it)
                        val accessToken = uri.getQueryParameter("oauth_token") ?: ""
                        val accessTokenSecret = uri.getQueryParameter("oauth_token_secret") ?: ""
                        val userId = uri.getQueryParameter("user_id") ?: ""
                        val screenName = uri.getQueryParameter("screen_name") ?: ""

                        Resource.Success(AccessTokenResponse(accessToken, accessTokenSecret, userId, screenName))

                    } ?: Resource.Error("getAccessToken service response body is null")
                }else{
                    /** 400 -> 599 Error status range **/
                    Timber.d("Response failed: %s", response.body().toString())
                    Resource.Error("Response failed with code: " + response.code())
                }
            }else{
                // not connected to internet
                Resource.Error("Couldn't reached to server. Please check your internet connection")
            }
        }catch (ex : Exception){
            Timber.d(ex)
            Resource.Error("getAccessToken Service connection error.")
        }
    }
}