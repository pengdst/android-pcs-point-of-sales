package io.github.pengdst.salescashier.data.remote.models


import com.google.gson.annotations.SerializedName

data class AuthData(
    @SerializedName("admin")
    val admin: Admin? = null,
    @SerializedName("token")
    val token: String? = null
)