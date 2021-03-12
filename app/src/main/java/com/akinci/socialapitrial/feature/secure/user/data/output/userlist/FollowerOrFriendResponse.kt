package com.akinci.socialapitrial.feature.secure.user.data.output.userlist

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FollowerOrFriendResponse(
    val next_cursor: Long,
    val next_cursor_str: String,
    val previous_cursor: Long,
    val previous_cursor_str: String,
    val total_count: Int?,
    val users: List<UserResponse>
)