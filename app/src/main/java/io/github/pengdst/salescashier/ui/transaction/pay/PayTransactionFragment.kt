package io.github.pengdst.salescashier.ui.transaction.pay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import io.github.pengdst.libs.ui.fragment.viewbinding.FragmentViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.databinding.FragmentPayTransactionBinding
import java.text.NumberFormat
import javax.inject.Inject

@AndroidEntryPoint
class PayTransactionFragment : Fragment() {

    private var sumOfPrice: Double = 0.0
    private val binding: FragmentPayTransactionBinding by viewBindings()

    private val args: PayTransactionFragmentArgs by navArgs()

    @Inject
    lateinit var numberFormat: NumberFormat

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
        }
    }
}