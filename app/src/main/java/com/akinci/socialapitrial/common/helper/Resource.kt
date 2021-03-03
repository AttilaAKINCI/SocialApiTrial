package com.akinci.socialapitrial.common.helper

//TODO Resource ismini cok bagdastiramadim: Response, DataResponse, ApiResponse
sealed class Resource<out T> {
    data class Success<T>(val data: T?) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    data class Loading(val message: String = "") : Resource<Nothing>()
}