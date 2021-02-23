package com.akinci.socialapitrial.feature.secure.userdetail.data.output

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Entities(
    val hashtags: List<Hashtag>?
)