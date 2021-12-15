package io.github.pengdst.salescashier.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.pengdst.salescashier.base.BaseAdapter
import io.github.pengdst.salescashier.data.remote.models.Product
import io.github.pengdst.salescashier.databinding.ItemProductBinding
import java.text.NumberFormat
import javax.inject.Inject

class ProductAdapter @Inject constructor() : BaseAdapter.Listadapter<Product, ProductAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product) =
        oldItem.hashCode() == newItem.hashCode()

    override fun areContentsTheSame(oldItem: Product, newItem: Product) =
        oldItem.toString() == newItem.toString()
}) {

    @Inject lateinit var numberFormat: NumberFormat

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            with(binding) {
                tvTitle.text = product.nama
                tvPrice.text = numberFormat.format(product.harga)

                onItemClickCallback?.let { clickCallback ->
                    btnDelete.setOnClickListener {
                        clickCallback.onItemClick(it, product, adapterPosition)
                    }

                    itemView.setOnClickListener {
                        clickCallback.onItemClick(it, product, adapterPosition)
                    }
                }
            }
        }

    }
}