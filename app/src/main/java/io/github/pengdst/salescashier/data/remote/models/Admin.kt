package io.github.pengdst.salescashier.data.remote.models


import com.google.gson.annotations.SerializedName

data class Admin(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("nama")
    val nama: String? = null
)