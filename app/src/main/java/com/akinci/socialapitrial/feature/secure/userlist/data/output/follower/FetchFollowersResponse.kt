package com.akinci.socialapitrial.feature.secure.userlist.data.output.follower

data class FetchFollowersResponse(
    val next_cursor: Long,
    val next_cursor_str: String,
    val previous_cursor: Int,
    val previous_cursor_str: String,
    val total_count: Any,
    val users: List<User>
)