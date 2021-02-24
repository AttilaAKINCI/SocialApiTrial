package com.akinci.socialapitrial.feature.secure.user.data.output.userlist

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    val id: Long,
    val name: String,
    val screen_name: String,
    val location: String?,
    val description: String,
    val followers_count: Int,
    val friends_count: Int,
    val verified: Boolean,
    val profile_background_image_url_https: String?,
    val profile_image_url_https: String?,
    val profile_banner_url: String?
)