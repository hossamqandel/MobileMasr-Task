package com.hossam.mobilemasrtask.auth.data.dto

import com.google.gson.annotations.SerializedName

data class TokenProvider(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
)
