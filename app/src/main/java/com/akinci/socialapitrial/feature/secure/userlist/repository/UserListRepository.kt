package com.akinci.socialapitrial.feature.secure.userlist.repository

import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.secure.userlist.data.output.follower.FetchFollowersResponse
import com.akinci.socialapitrial.feature.secure.userlist.data.output.singout.SignOutResponse

interface UserListRepository {
    suspend fun signOut(): Resource<SignOutResponse>
    suspend fun fetchFollowers(): Resource<FetchFollowersResponse>
    suspend fun fetchFollowings(): Resource<Nothing>
    suspend fun getUserInfo(): Resource<Nothing>
}