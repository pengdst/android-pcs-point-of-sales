package io.github.pengdst.salescashier.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.pengdst.libs.ui.fragment.viewbinding.FragmentViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.databinding.FragmentReportBinding
import java.text.NumberFormat
import javax.inject.Inject

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private val binding: FragmentReportBinding by viewBindings()

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

        binding.tvTotalRevenue.text = numberFormat.format(0)
    }
}