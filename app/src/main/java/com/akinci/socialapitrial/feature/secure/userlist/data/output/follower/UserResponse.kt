package com.akinci.socialapitrial.feature.secure.userlist.data.output.follower

data class UserResponse(
    val id: Int,
    val name: String,
    val screen_name: String,
    val location: String,
    val description: String,
    val followers_count: Int,
    val friends_count: Int,
    val verified: Boolean,
    val profile_background_image_url_https: String,
    val profile_image_url_https: String,
    val profile_banner_url: String
)