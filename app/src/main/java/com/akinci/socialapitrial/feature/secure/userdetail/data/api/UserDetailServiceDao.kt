package com.akinci.socialapitrial.feature.secure.userdetail.data.api

import com.akinci.socialapitrial.common.network.RestConfig
import com.akinci.socialapitrial.feature.secure.userdetail.data.output.UserTimeLineResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserDetailServiceDao {

    @GET(RestConfig.REQUEST_GET_TIME_LINE)
    suspend fun getUserTimeLine(
            @Query("user_id") userId : Long,
            @Query("count") count : Int
    ) : Response<List<UserTimeLineResponse>>

}