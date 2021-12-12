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

    inner class ViewHolder(private val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: TransactionItem?) {
            with(binding) {
                tvTitle.text = transaction?.product?.nama
                tvPrice.text = numberFormat.format(transaction?.product?.harga)
                tilAmount.editText?.setText("${transaction?.amount ?: 0}")

                btnIncrease.setOnClickListener {
                    transaction?.amount = transaction?.amount?.inc() ?: 0
                    notifyItemChanged(adapterPosition)
                }

                btnDecrease.setOnClickListener {
                    transaction?.amount = transaction?.amount?.dec() ?: 0
                    notifyItemChanged(adapterPosition)
                }

                tilAmount.editText?.doOnTextChanged { _, _, _, _ ->
                    onChanged?.invoke(currentList)
                }
            }
        }

    }
}