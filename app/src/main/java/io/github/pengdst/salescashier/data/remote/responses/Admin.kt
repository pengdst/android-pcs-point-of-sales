package io.github.pengdst.salescashier.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Admin(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("nama")
    val nama: String? = null
)