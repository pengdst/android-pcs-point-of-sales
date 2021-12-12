package io.github.pengdst.salescashier.ui.transaction

import io.github.pengdst.salescashier.data.remote.models.Product

data class TransactionItem (
    val product: Product,
    var amount: Int = 0
)
