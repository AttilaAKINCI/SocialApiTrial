package com.akinci.socialapitrial.feature.secure.userdetail.data.output

import com.akinci.socialapitrial.feature.secure.userlist.data.output.follower.UserResponse
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserTimeLineResponse(
    val id: Long,
    val text: String,
    val entities: Entities?,
    val user: UserResponse,
    val created_at: String
)