package io.github.pengdst.salescashier.ui.transaction.pay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import io.github.pengdst.libs.ui.fragment.viewbinding.FragmentViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.data.local.prefs.Session
import io.github.pengdst.salescashier.data.remote.models.Transaction
import io.github.pengdst.salescashier.data.remote.requests.CreateItemTransactionRequest
import io.github.pengdst.salescashier.data.remote.requests.CreateTransactionRequest
import io.github.pengdst.salescashier.data.remote.responses.ErrorResponse
import io.github.pengdst.salescashier.data.remote.routes.SalesRoute
import io.github.pengdst.salescashier.databinding.FragmentPayTransactionBinding
import io.github.pengdst.salescashier.utils.longToast
import io.github.pengdst.salescashier.utils.shortToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.NumberFormat
import javax.inject.Inject

@AndroidEntryPoint
class PayTransactionFragment : Fragment() {

    private var sumOfPrice: Double = 0.0
    private val binding: FragmentPayTransactionBinding by viewBindings()

    private val args: PayTransactionFragmentArgs by navArgs()

    @Inject
    lateinit var numberFormat: NumberFormat

    @Inject
    lateinit var session: Session

    @Inject
    lateinit var salesRoute: SalesRoute

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            sumOfPrice = args.productTransactions.toList().sumOf {
                it.amount * (it.product.harga ?: 0.0)
            }
            tvTotalPay.text = numberFormat.format(sumOfPrice)

            tilPay.editText?.doOnTextChanged { text, _, _, _ ->
                text.toString().toDoubleOrNull()?.let { payBills ->
                    val returnMoney = payBills - sumOfPrice
                    labelKembalian.text = if (returnMoney < 0) "Uang Anda Kurang" else "Kembalian"
                    tvKembalian.text = numberFormat.format(returnMoney)
                }
            }

            btnPay.setOnClickListener {
                payTransaction()
            }
        }
    }

    private fun payTransaction() {
        binding.btnPay.isEnabled = false
        lifecycleScope.launchWhenResumed {
            try {
                withContext(Dispatchers.IO) {
                    val response = salesRoute.createTransaction(
                        CreateTransactionRequest(
                            adminId = session.getAuthUser().id,
                            total = args.productTransactions.size
                        )
                    )
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        val products = responseBody?.data

                        withContext(Dispatchers.Main) {
                            shortToast(responseBody?.message ?: "Success Create Transaction")
                            products?.let { createTransactionItem(it) }
                        }
                    } else {
                        val errorBody = ErrorResponse.fromErrorBody(response.errorBody())
                        withContext(Dispatchers.Main) {
                            longToast(errorBody.message ?: "Create Transaction Failed")
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                binding.btnPay.isEnabled = true
            }
        }
    }

    private fun createTransactionItem(transaction: Transaction) {
        binding.btnPay.isEnabled = false
        lifecycleScope.launchWhenResumed {
            try {
                args.productTransactions.forEach {
                    withContext(Dispatchers.IO) {
                        val response = salesRoute.createTransactionItem(
                            CreateItemTransactionRequest(
                                productId = it.product.id,
                                harga = it.product.harga,
                                qty = it.amount,
                                transaksiId = transaction.id
                            )
                        )
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            withContext(Dispatchers.Main) {
                                shortToast(responseBody?.message ?: "Success Create Transaction Item")
                            }
                        } else {
                            val errorBody = ErrorResponse.fromErrorBody(response.errorBody())
                            withContext(Dispatchers.Main) {
                                longToast(errorBody.message ?: "Create Transaction Item Failed")
                            }
                        }
                    }
                }
                requireActivity().onBackPressed()
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                binding.btnPay.isEnabled = true
            }
        }
    }
}