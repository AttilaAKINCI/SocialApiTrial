package com.akinci.socialapitrial.feature.secure.userlist.data.api

import com.akinci.socialapitrial.common.network.RestConfig
import com.akinci.socialapitrial.feature.secure.userlist.data.output.follower.FetchFollowersResponse
import com.akinci.socialapitrial.feature.secure.userlist.data.output.singout.SignOutResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserListServiceDao {
    @POST(RestConfig.REQUEST_SIGN_OUT)
    suspend fun signOut() : Response<SignOutResponse>

    @GET(RestConfig.REQUEST_FETCH_FOLLOWERS)
    suspend fun fetchFollowers(
            @Query("cursor") cursor : Int
    ) : Response<FetchFollowersResponse>
}