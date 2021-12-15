package io.github.pengdst.salescashier.ui.product

import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.github.pengdst.libs.ui.fragment.viewbinding.FragmentViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.R
import io.github.pengdst.salescashier.data.remote.responses.ErrorResponse
import io.github.pengdst.salescashier.data.remote.routes.SalesRoute
import io.github.pengdst.salescashier.databinding.FragmentProductBinding
import io.github.pengdst.salescashier.utils.longToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


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

        productAdapter.setOnItemClickListener { view, model, _ ->
            when(view.id) {
                R.id.btn_delete -> MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete Product")
                    .setMessage("Anda ingin menghapus ${model.nama}?")
                    .setPositiveButton("Delete"
                    ) { dialog, _ ->
                        deleteProduct(model.id ?: -1)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel"
                    ) { dialog, _ -> dialog.dismiss() }
                    .show()
                else -> {
                    findNavController().navigate(ProductFragmentDirections.actionProductFragmentToAddProductFragment(model))
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTotalProduct.text = TextUtils.concat("0", " ",
            SpannableString("ITEM").apply {
                setSpan(RelativeSizeSpan(0.5f), 0, 4, 0)
            }
        )
        with(binding) {
            rvProducts.adapter = productAdapter

            btnAdd.setOnClickListener {
                findNavController().navigate(ProductFragmentDirections
                    .actionProductFragmentToAddProductFragment())
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

    private fun deleteProduct(productId: Int) {
        lifecycleScope.launchWhenCreated {
            try {
                withContext(Dispatchers.IO) {
                    val response = salesRoute.deleteProduct(productId)
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        val products = responseBody?.data ?: emptyList()

                        withContext(Dispatchers.Main) {
                            longToast(responseBody?.message ?: "Show Products Failed")
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