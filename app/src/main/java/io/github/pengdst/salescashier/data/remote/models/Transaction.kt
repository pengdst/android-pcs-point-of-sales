package io.github.pengdst.salescashier.data.remote.models


import com.google.gson.annotations.SerializedName
import java.util.*

data class Transaction(
    @SerializedName("admin_id")
    var adminId: Int? = null,
    @SerializedName("date_in_milis")
    var dateInMilis: Long? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("tanggal")
    var tanggal: String? = null,
    @SerializedName("total")
    var total: Int? = null
) {

    var items: List<TransactionItem> = emptyList()

    val date get() = Date(dateInMilis?.times(1000) ?: 0)

}