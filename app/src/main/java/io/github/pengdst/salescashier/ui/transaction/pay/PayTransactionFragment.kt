package io.github.pengdst.salescashier.ui.transaction.pay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.pengdst.libs.ui.fragment.viewbinding.FragmentViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.databinding.FragmentPayTransactionBinding

class PayTransactionFragment : Fragment() {

    private val binding: FragmentPayTransactionBinding by viewBindings()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }
}