package com.akinci.socialapitrial.feature.secure.userlist.repository

import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.secure.userlist.data.output.follower.FollowerOrFriendResponse
import com.akinci.socialapitrial.feature.secure.userlist.data.output.follower.UserResponse
import com.akinci.socialapitrial.feature.secure.userlist.data.output.singout.SignOutResponse

interface UserListRepository {
    suspend fun signOut(): Resource<SignOutResponse>
    suspend fun fetchFollowers(cursor : Long): Resource<FollowerOrFriendResponse>
    suspend fun fetchFollowings(cursor : Long): Resource<FollowerOrFriendResponse>
    suspend fun getUserInfo(userId: Int, userName: String): Resource<UserResponse>
}