package com.akinci.socialapitrial.feature.secure.userlist.repository

import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.common.repository.BaseRepositoryImpl
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.feature.secure.userlist.data.api.UserListServiceDao
import com.akinci.socialapitrial.feature.secure.userlist.data.output.follower.FollowerOrFriendResponse
import com.akinci.socialapitrial.feature.secure.userlist.data.output.follower.UserResponse
import com.akinci.socialapitrial.feature.secure.userlist.data.output.singout.SignOutResponse
import javax.inject.Inject

class UserListRepositoryImpl @Inject constructor(
        private val userListServiceDao: UserListServiceDao,
        private val networkChecker: NetworkChecker
) : BaseRepositoryImpl(networkChecker), UserListRepository {

    override suspend fun fetchFollowers(cursor : Long): Resource<FollowerOrFriendResponse> {
        return callService { userListServiceDao.fetchFollowers(cursor) }
    }

    override suspend fun fetchFollowings(cursor : Long): Resource<FollowerOrFriendResponse> {
        return callService { userListServiceDao.fetchFriends(cursor) }
    }

    override suspend fun getUserInfo(userId: Int, userName: String): Resource<UserResponse> {
        return callService { userListServiceDao.getUserInfo(userId, userName) }
    }

    override suspend fun signOut(): Resource<SignOutResponse> {
        return callService { userListServiceDao.signOut() }
    }

}