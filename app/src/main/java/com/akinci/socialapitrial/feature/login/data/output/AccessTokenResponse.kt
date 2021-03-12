package com.akinci.socialapitrial.feature.login.data.output

data class AccessTokenResponse (
    val oauth_token: String,
    val oauth_token_secret: String,
    val user_id: String,
    val screen_name: String
)