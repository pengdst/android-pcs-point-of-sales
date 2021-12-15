package io.github.pengdst.salescashier.ui.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.pengdst.salescashier.databinding.ItemTransactionBinding
import java.text.NumberFormat
import javax.inject.Inject

class TransactionAdapter @Inject constructor() : ListAdapter<TransactionItem, TransactionAdapter.ViewHolder>(object : DiffUtil.ItemCallback<TransactionItem>() {
    override fun areItemsTheSame(oldItem: TransactionItem, newItem: TransactionItem) =
        oldItem.hashCode() == newItem.hashCode()

    override fun areContentsTheSame(oldItem: TransactionItem, newItem: TransactionItem) =
        oldItem.toString() == newItem.toString()
}) {

    @Inject lateinit var numberFormat: NumberFormat

    var onChanged: ((List<TransactionItem>)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun finish(finishedList: (List<TransactionItem>) -> Unit) {
        finishedList(currentList.filter { it.amount > 0 })
    }

    inner class ViewHolder(private val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: TransactionItem?) {
            with(binding) {
                tvTitle.text = transaction?.product?.nama
                tvPrice.text = numberFormat.format(transaction?.product?.harga)
                tilAmount.editText?.setText("${transaction?.amount ?: 0}")

                btnIncrease.setOnClickListener {
                    val inc = tilAmount.editText?.text.toString().toIntOrNull()?.inc() ?: 0
                    tilAmount.editText?.setText(inc.toString())
                }

                btnDecrease.setOnClickListener {
                    val dec = tilAmount.editText?.text.toString().toIntOrNull()?.dec() ?: 0
                    tilAmount.editText?.setText(dec.toString())
                }

                tilAmount.editText?.doOnTextChanged { text, _, _, _ ->
                    text.toString().toIntOrNull()?.let { amount ->
                        transaction?.amount = amount
                        if (amount < 0) tilAmount.editText?.setText("0")
                        onChanged?.invoke(currentList)
                    } ?: tilAmount.editText?.setText("0")
                }
            }
        }

    }
}