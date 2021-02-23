package com.akinci.socialapitrial.feature.secure.userlist.data.output.singout

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignOutResponse (
    val access_token : String
)