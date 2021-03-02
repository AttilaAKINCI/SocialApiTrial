package com.akinci.socialapitrial.feature.secure.user.data.api

import com.akinci.socialapitrial.common.network.RestConfig
import com.akinci.socialapitrial.feature.secure.user.data.output.userdetail.UserTimeLineResponse
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.FollowerOrFriendResponse
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserServiceDao {

    // TODO Su constant class a gerek var mi bilemedim
    @GET(RestConfig.REQUEST_GET_TIME_LINE)
    suspend fun getUserTimeLine(
            @Query("user_id") userId : Long,
            @Query("count") count : Int
    ) : Response<List<UserTimeLineResponse>>

    @GET(RestConfig.REQUEST_FETCH_FOLLOWERS)
    suspend fun fetchFollowers(
            @Query("cursor") cursor : Long
    ) : Response<FollowerOrFriendResponse>

    @GET(RestConfig.REQUEST_FETCH_FOLLOWINGS)
    suspend fun fetchFriends(
            @Query("cursor") cursor : Long
    ) : Response<FollowerOrFriendResponse>

    @GET(RestConfig.REQUEST_GET_USER_INFO)
    suspend fun getUserInfo(
            @Query("user_id") userId : Long,
            @Query("screen_name") screenName : String
    ) : Response<UserResponse>
}