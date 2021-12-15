package io.github.pengdst.salescashier.data.remote.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    @SerializedName("admin_id")
    val adminId: Int? = null,
    @SerializedName("harga")
    val harga: Double? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("nama")
    val nama: String? = null,
    @SerializedName("stock")
    val stock: Int? = null
) : Parcelable