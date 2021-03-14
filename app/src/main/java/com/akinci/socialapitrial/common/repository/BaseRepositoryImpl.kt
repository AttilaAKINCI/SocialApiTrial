package com.akinci.socialapitrial.common.repository

import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.network.NetworkChecker
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception

open class BaseRepositoryImpl constructor(
    private val networkChecker: NetworkChecker
) {
    /** Service generic network checker **/

    // CallService generify for JSON Responses
    suspend fun <T> callService(
            retrofitServiceAction : suspend () -> Response<T>
    ) : Resource<T> {
        return try {
            if (networkChecker.isNetworkConnected()) {
                // internet connection is established.
                // invoke service generic part.
                val response = retrofitServiceAction.invoke()
                if (response.isSuccessful) {
                    /** 200 -> 299 Error status range **/
                    response.body()?.let {
                        // handle response body
                        Resource.Success(it)
                    } ?: Resource.Error("Service response body is null")
                }else {
                    /** 400 -> 599 Error status range **/
                    Timber.d("Service response failed: %s", response.errorBody().toString())
                    Resource.Error("Service response failed with code: " + response.code())
                }
            }else{
                // not connected to internet
                Timber.d("Couldn't reached to server. Please check your internet connection")
                Resource.Error("Couldn't reached to server. Please check your internet connection")
            }
        }catch (ex: Exception){
            Timber.d(ex)
            Resource.Error("UnExpected Service Exception.")
        }
    }

    // CallService generify for Any Response that will be mapped another object
    suspend fun <T, M> callService(
            retrofitServiceAction : suspend () -> Response<T>,
            customResponseMappingAction : ((T) -> Resource<M>)
    ) : Resource<M> {
        return try {
            if (networkChecker.isNetworkConnected()) {
                // internet connection is established.
                // invoke service generic part.
                val response = retrofitServiceAction.invoke()
                if (response.isSuccessful) {
                    /** 200 -> 299 Error status range **/
                    response.body()?.let {
                        // handle response body
                        customResponseMappingAction.invoke(it)
                    } ?: Resource.Error("Service response body is null")
                }else {
                    /** 400 -> 599 Error status range **/
                    Timber.d("Service response failed: %s", response.errorBody().toString())
                    Resource.Error("Service response failed with code: " + response.code())
                }
            }else{
                // not connected to internet
                Timber.d("Couldn't reached to server. Please check your internet connection")
                Resource.Error("Couldn't reached to server. Please check your internet connection")
            }
        }catch (ex: Exception){
            Timber.d(ex)
            Resource.Error("UnExpected Service Exception.")
        }
    }



}