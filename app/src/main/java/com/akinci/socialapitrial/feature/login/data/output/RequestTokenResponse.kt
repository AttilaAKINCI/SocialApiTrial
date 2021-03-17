package com.akinci.socialapitrial.feature.login.data.output

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestTokenResponse (
    val oauth_token: String,
    val oauth_token_secret: String
)