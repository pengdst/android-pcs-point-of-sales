package io.github.pengdst.salescashier.data.remote.requests


import com.google.gson.annotations.SerializedName

data class CreateItemTransactionRequest(
    @SerializedName("harga")
    var harga: Double? = null,
    @SerializedName("product_id")
    var productId: Int? = null,
    @SerializedName("qty")
    var qty: Int? = null,
    @SerializedName("transaksi_id")
    var transaksiId: Int? = null
)