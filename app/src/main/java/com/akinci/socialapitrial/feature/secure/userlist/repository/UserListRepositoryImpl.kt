package com.akinci.socialapitrial.feature.secure.userlist.repository

import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.common.repository.BaseRepositoryImpl
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.feature.secure.userlist.data.api.UserListServiceDao
import com.akinci.socialapitrial.feature.secure.userlist.data.output.follower.FetchFollowersResponse
import com.akinci.socialapitrial.feature.secure.userlist.data.output.singout.SignOutResponse
import javax.inject.Inject

class UserListRepositoryImpl @Inject constructor(
        private val userListServiceDao: UserListServiceDao,
        private val networkChecker: NetworkChecker,
        private val sharedPreferences: Preferences
) : BaseRepositoryImpl(networkChecker), UserListRepository {

    override suspend fun fetchFollowers(): Resource<FetchFollowersResponse> {
        return Resource.Error("")
    }

    override suspend fun fetchFollowings(): Resource<Nothing> {
        return Resource.Error("")
    }

    override suspend fun getUserInfo(): Resource<Nothing> {
        return Resource.Error("")
    }

    override suspend fun signOut(): Resource<SignOutResponse> {
        return callService { userListServiceDao.signOut() }
    }

}