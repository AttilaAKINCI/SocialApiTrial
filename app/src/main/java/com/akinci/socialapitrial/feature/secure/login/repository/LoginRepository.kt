package com.akinci.socialapitrial.feature.secure.login.repository

import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.secure.login.data.output.SignOutResponse

interface LoginRepository {
    suspend fun signOut(): Resource<SignOutResponse>
}