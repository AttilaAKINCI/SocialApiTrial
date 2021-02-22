package com.akinci.socialapitrial.feature.login.data.output

data class RequestTokenResponse (
    val oauth_token: String,
    val oauth_token_secret: String
)