package io.github.pengdst.salescashier.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.pengdst.libs.ui.fragment.viewbinding.FragmentViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.data.remote.responses.ErrorResponse
import io.github.pengdst.salescashier.data.remote.routes.SalesRoute
import io.github.pengdst.salescashier.databinding.FragmentTransactionBinding
import io.github.pengdst.salescashier.utils.longToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.NumberFormat
import javax.inject.Inject

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private val binding: FragmentTransactionBinding by viewBindings()

    @Inject lateinit var transactionAdapter: TransactionAdapter
    @Inject lateinit var numberFormat: NumberFormat
    @Inject lateinit var salesRoute: SalesRoute

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        transactionAdapter.onChanged = { transactions ->
            binding.tvTotalProduct.text = numberFormat.format(transactions.sumOf { it.amount * (it.product.harga ?: 0.0) })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            tvTotalProduct.text = numberFormat.format(0)
            rvTransactions.adapter = transactionAdapter

            btnPay.setOnClickListener {
                transactionAdapter.finish {
                    findNavController().navigate(TransactionFragmentDirections
                        .actionTransactionFragmentToPayTransactionFragment(it.toTypedArray()))
                }
            }
        }

        getProducts()
    }

    private fun getProducts() {
        lifecycleScope.launchWhenCreated {
            try {
                withContext(Dispatchers.IO) {
                    val response = salesRoute.getProducts()
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        val products = responseBody?.data ?: emptyList()

                        withContext(Dispatchers.Main) {
                            binding.tvTotalProduct.text = numberFormat.format(0)
                            transactionAdapter.submitList(products.map { TransactionItem(it) })
                        }
                    } else {
                        val errorBody = ErrorResponse.fromErrorBody(response.errorBody())
                        withContext(Dispatchers.Main) {
                            longToast(errorBody.message ?: "Show Products Failed")
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}