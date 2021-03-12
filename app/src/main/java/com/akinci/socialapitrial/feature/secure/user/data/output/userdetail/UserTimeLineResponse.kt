package com.akinci.socialapitrial.feature.secure.user.data.output.userdetail

import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserTimeLineResponse(
        val id: Long,
        val text: String,
        val entities: Entities?,
        val user: UserResponse,
        val created_at: String
)