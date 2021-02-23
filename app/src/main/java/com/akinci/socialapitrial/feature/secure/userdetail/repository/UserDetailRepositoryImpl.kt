package com.akinci.socialapitrial.feature.secure.userdetail.repository

import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.common.repository.BaseRepositoryImpl
import com.akinci.socialapitrial.feature.secure.userdetail.data.api.UserDetailServiceDao
import com.akinci.socialapitrial.feature.secure.userdetail.data.output.UserTimeLineResponse
import javax.inject.Inject

class UserDetailRepositoryImpl @Inject constructor(
        private val userDetailServiceDao: UserDetailServiceDao,
        private val networkChecker: NetworkChecker
) : BaseRepositoryImpl(networkChecker), UserDetailRepository {

    override suspend fun getUserTimeLine(userId: Long, count: Int): Resource<List<UserTimeLineResponse>> {
        return callService { userDetailServiceDao.getUserTimeLine(userId, count) }
    }

}