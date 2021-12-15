package io.github.pengdst.salescashier.data.remote.models


import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("admin_id")
    var adminId: Int? = null,
    @SerializedName("date_in_milis")
    var dateInMilis: Int? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("tanggal")
    var tanggal: String? = null,
    @SerializedName("total")
    var total: Int? = null
)