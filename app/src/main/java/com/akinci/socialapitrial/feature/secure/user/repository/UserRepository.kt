package com.akinci.socialapitrial.feature.secure.user.repository

import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.secure.user.data.output.userdetail.UserTimeLineResponse
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.FollowerOrFriendResponse
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse

interface UserRepository {
    suspend fun getUserTimeLine(userId: Long, count: Int) : Resource<List<UserTimeLineResponse>>
    suspend fun fetchFollowers(cursor : Long): Resource<FollowerOrFriendResponse>
    suspend fun fetchFollowings(cursor : Long): Resource<FollowerOrFriendResponse>
    suspend fun getUserInfo(userId: Long, userName: String): Resource<UserResponse>
}