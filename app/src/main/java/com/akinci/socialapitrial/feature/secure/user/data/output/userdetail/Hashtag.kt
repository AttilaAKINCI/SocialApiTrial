package com.akinci.socialapitrial.feature.secure.user.data.output.userdetail

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Hashtag(
    val text: String
)