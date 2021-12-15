package io.github.pengdst.salescashier.ui.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.pengdst.salescashier.base.BaseAdapter
import io.github.pengdst.salescashier.data.remote.models.Transaction
import io.github.pengdst.salescashier.databinding.ItemReportBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ReportAdapter @Inject constructor() : BaseAdapter.Listadapter<Transaction, ReportAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) =
        oldItem.hashCode() == newItem.hashCode()

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) =
        oldItem.toString() == newItem.toString()
}) {

    @Inject lateinit var numberFormat: NumberFormat

    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemReportBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemReportBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            with(binding) {
                tvStructId.text = String.format("#%04d", transaction.id)
                tvDate.text = dateFormat.format(transaction.date)

                tvTotalIncome.text = numberFormat.format(transaction.items.sumOf { (it.hargaSaatTransaksi ?: 0) * (it.qty ?: 0) })
            }
        }

    }
}