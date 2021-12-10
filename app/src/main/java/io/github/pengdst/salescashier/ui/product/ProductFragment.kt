package io.github.pengdst.salescashier.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.pengdst.libs.ui.fragment.viewbinding.FragmentViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.data.remote.responses.ErrorResponse
import io.github.pengdst.salescashier.data.remote.routes.SalesRoute
import io.github.pengdst.salescashier.databinding.FragmentProductBinding
import io.github.pengdst.salescashier.utils.longToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

import android.text.style.RelativeSizeSpan

import android.text.SpannableString
import android.text.TextUtils


@AndroidEntryPoint
class ProductFragment : Fragment() {

    private val binding: FragmentProductBinding by viewBindings()

    @Inject lateinit var productAdapter: ProductAdapter
    @Inject lateinit var salesRoute: SalesRoute

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvProducts.adapter = productAdapter

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
                            binding.tvTotalProduct.text = TextUtils.concat(products.size.toString(), " ",
                                SpannableString("ITEM").apply {
                                    setSpan(RelativeSizeSpan(0.5f), 0, 4, 0)
                                }
                            )
                            productAdapter.submitList(products)
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