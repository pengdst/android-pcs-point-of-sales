package io.github.pengdst.salescashier.data.remote.requests


import com.google.gson.annotations.SerializedName

data class UpdateProductRequest(
    @SerializedName("admin_id")
    var adminId: Int? = null,
    @SerializedName("harga")
    var harga: Int? = null,
    @SerializedName("nama")
    var nama: String? = null,
    @SerializedName("stock")
    var stock: Int? = null
)