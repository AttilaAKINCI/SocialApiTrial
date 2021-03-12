package com.akinci.socialapitrial.feature.secure.login.repository

import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.common.repository.BaseRepositoryImpl
import com.akinci.socialapitrial.feature.secure.login.data.api.LoginServiceDao
import com.akinci.socialapitrial.feature.secure.login.data.output.SignOutResponse
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
        private val loginServiceDao: LoginServiceDao,
        networkChecker: NetworkChecker
) : BaseRepositoryImpl(networkChecker), LoginRepository {

    override suspend fun signOut(): Resource<SignOutResponse> {
        return callService { loginServiceDao.signOut() }
    }

}