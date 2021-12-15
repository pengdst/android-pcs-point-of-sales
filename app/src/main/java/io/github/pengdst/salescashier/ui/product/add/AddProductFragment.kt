package io.github.pengdst.salescashier.ui.product.add

import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.pengdst.libs.ui.fragment.viewbinding.FragmentViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.data.local.prefs.Session
import io.github.pengdst.salescashier.data.remote.requests.CreateProductRequest
import io.github.pengdst.salescashier.data.remote.responses.ErrorResponse
import io.github.pengdst.salescashier.data.remote.routes.SalesRoute
import io.github.pengdst.salescashier.databinding.FragmentAddProductBinding
import io.github.pengdst.salescashier.utils.longToast
import io.github.pengdst.salescashier.utils.shortToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AddProductFragment : Fragment() {

    private val binding: FragmentAddProductBinding by viewBindings()

    @Inject lateinit var session: Session
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

        with(binding) {
            btnSave.setOnClickListener {
                val request = CreateProductRequest(
                    adminId = session.getAuthUser().id,
                    harga = tilPrice.editText?.text.toString().toIntOrNull(),
                    nama = tilName.editText?.text.toString().trim(),
                    stock = 20
                )
                createProducts(request)
            }
        }
    }

    private fun createProducts(request: CreateProductRequest) {
        binding.btnSave.isEnabled = false
        lifecycleScope.launchWhenCreated {
            try {
                withContext(Dispatchers.IO) {
                    val response = salesRoute.createProduct(request)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        withContext(Dispatchers.Main) {
                            shortToast(responseBody?.message ?: "Create Product Success")
                            requireActivity().onBackPressed()
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
            } finally {
                binding.btnSave.isEnabled = true
            }
        }
    }
}