package com.akinci.socialapitrial.feature.login.repository

import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.feature.login.data.api.LoginServiceDao
import com.akinci.socialapitrial.feature.login.data.output.RequestTokenResponse
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginServiceDao,
    private val networkChecker: NetworkChecker
) : LoginRepository {

    override suspend fun requestToken(): Resource<RequestTokenResponse> {
        return try {
            if (networkChecker.isNetworkConnected()) {
                //internet connection is established.

                val response = loginService.requestToken()
                if (response.isSuccessful) {
                    /** 200 -> 299 Error status range **/
                    response.body()?.let {
                        val parameters = response.body()?.split("&")
                        val authToken = parameters?.get(0)?.split("=")?.get(1) ?: ""
                        val authTokenSecret = parameters?.get(1)?.split("=")?.get(1) ?: ""

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
            Resource.Error("Service connection error.")
        }
    }
}