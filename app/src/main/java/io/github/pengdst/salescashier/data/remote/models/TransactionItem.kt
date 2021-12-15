package io.github.pengdst.salescashier.data.remote.models


import com.google.gson.annotations.SerializedName

data class TransactionItem(
    @SerializedName("harga_saat_transaksi")
    var hargaSaatTransaksi: Int? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("produk_id")
    var produkId: Int? = null,
    @SerializedName("qty")
    var qty: Int? = null,
    @SerializedName("sub_total")
    var subTotal: Int? = null,
    @SerializedName("transaksi_id")
    var transaksiId: Int? = null
) {

    val priceTotal get() = (hargaSaatTransaksi ?: 0) * (qty ?: 0)

}