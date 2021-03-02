package com.akinci.socialapitrial.feature.secure.user.repository

import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.common.repository.BaseRepositoryImpl
import com.akinci.socialapitrial.feature.secure.user.data.api.UserServiceDao
import com.akinci.socialapitrial.feature.secure.user.data.output.userdetail.UserTimeLineResponse
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.FollowerOrFriendResponse
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse
import kotlinx.coroutines.delay
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
        private val userServiceDao: UserServiceDao,
        networkChecker: NetworkChecker
) : BaseRepositoryImpl(networkChecker), UserRepository {

    override suspend fun fetchFollowers(cursor : Long): Resource<FollowerOrFriendResponse> {
        return callService { userServiceDao.fetchFollowers(cursor) }
    }

    override suspend fun fetchFollowings(cursor : Long): Resource<FollowerOrFriendResponse> {
        return callService { userServiceDao.fetchFriends(cursor) }
    }

    override suspend fun getUserInfo(userId: Long, userName: String): Resource<UserResponse> {
        return callService { userServiceDao.getUserInfo(userId, userName) }
    }

    override suspend fun getUserTimeLine(userId: Long, count: Int): Resource<List<UserTimeLineResponse>> {
        return callService { userServiceDao.getUserTimeLine(userId, count) }
    }

}