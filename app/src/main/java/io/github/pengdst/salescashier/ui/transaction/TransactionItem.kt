package io.github.pengdst.salescashier.ui.transaction

import android.os.Parcelable
import io.github.pengdst.salescashier.data.remote.models.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionItem (
    val product: Product,
    var amount: Int = 0
) : Parcelable
