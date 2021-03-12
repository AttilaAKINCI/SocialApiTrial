package com.akinci.socialapitrial.feature.secure.login.data.output

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignOutResponse (
    val access_token : String
)