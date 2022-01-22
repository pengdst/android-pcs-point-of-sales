package io.github.pengdst.salescashier.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.pengdst.libs.ui.fragment.viewbinding.FragmentViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.data.remote.models.Product
import io.github.pengdst.salescashier.databinding.FragmentTransactionBinding
import io.github.pengdst.salescashier.ui.product.ProductViewModel
import io.github.pengdst.salescashier.utils.longToast
import java.text.NumberFormat
import javax.inject.Inject

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private val binding: FragmentTransactionBinding by viewBindings()

    @Inject lateinit var transactionAdapter: TransactionAdapter
    @Inject lateinit var numberFormat: NumberFormat
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        productViewModel.productsViewModel.observe(viewLifecycleOwner) {
            when(it) {
                is ProductViewModel.State.Failed -> longToast(it.message)
                is ProductViewModel.State.Success -> updateProducts(it.data)
                is ProductViewModel.State.Loading -> Unit
                is ProductViewModel.State.SuccessCreate -> Unit
                is ProductViewModel.State.SuccessDelete -> Unit
                is ProductViewModel.State.SuccessUpdate -> Unit
            }
        }

        transactionAdapter.onChanged = { transactions ->
            binding.btnPay.isEnabled = transactions.sumOf { it.amount * (it.product.harga ?: 0.0) } > 0
            binding.tvTotalProduct.text = numberFormat.format(transactions.sumOf { it.amount * (it.product.harga ?: 0.0) })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            tvTotalProduct.text = numberFormat.format(0)
            rvTransactions.adapter = transactionAdapter

            btnPay.isEnabled = transactionAdapter.currentList.sumOf { it.amount * (it.product.harga ?: 0.0) } > 0
            btnPay.setOnClickListener {
                transactionAdapter.finish {
                    findNavController().navigate(TransactionFragmentDirections
                        .actionTransactionFragmentToPayTransactionFragment(it.toTypedArray()))
                }
            }
        }

        productViewModel.getProducts()
    }

    private fun updateProducts(products: List<Product>) {
        binding.tvTotalProduct.text = numberFormat.format(0)
        transactionAdapter.submitList(products.map { TransactionItem(it) })
    }
}