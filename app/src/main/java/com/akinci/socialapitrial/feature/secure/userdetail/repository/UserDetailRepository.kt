package com.akinci.socialapitrial.feature.secure.userdetail.repository

import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.secure.userdetail.data.output.UserTimeLineResponse

interface UserDetailRepository {
    suspend fun getUserTimeLine(userId: Long, count: Int) : Resource<List<UserTimeLineResponse>>
}