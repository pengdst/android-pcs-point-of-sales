package io.github.pengdst.salescashier.data.remote.requests


import com.google.gson.annotations.SerializedName

data class CreateTransactionRequest(
    @SerializedName("admin_id")
    var adminId: Int? = null,
    @SerializedName("total")
    var total: Int? = null
)