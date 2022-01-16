package io.github.pengdst.salescashier.ui.product.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import io.github.pengdst.libs.ui.fragment.viewbinding.FragmentViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.R
import io.github.pengdst.salescashier.data.remote.models.Product
import io.github.pengdst.salescashier.data.remote.requests.CreateProductRequest
import io.github.pengdst.salescashier.data.remote.requests.UpdateProductRequest
import io.github.pengdst.salescashier.databinding.FragmentAddProductBinding
import io.github.pengdst.salescashier.ui.product.ProductViewModel
import io.github.pengdst.salescashier.utils.longToast
import io.github.pengdst.salescashier.utils.shortToast

@AndroidEntryPoint
class AddProductFragment : Fragment() {

    private val binding: FragmentAddProductBinding by viewBindings()

    private val productViewModel: ProductViewModel by viewModels()

    private val args: AddProductFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel.productsViewModel.observe(viewLifecycleOwner) {
            binding.btnSave.isEnabled = true
            when(it) {
                is ProductViewModel.State.Failed -> longToast(it.message)
                is ProductViewModel.State.Loading -> binding.btnSave.isEnabled = false
                is ProductViewModel.State.SuccessCreate -> {
                    shortToast(it.message)
                    requireActivity().onBackPressed()
                }
                is ProductViewModel.State.SuccessUpdate -> shortToast(it.message)
                is ProductViewModel.State.SuccessDelete -> Unit
                is ProductViewModel.State.Success -> Unit
            }
        }

        showProducts(args.product)
        with(binding) {
            btnSave.setOnClickListener {
                if (args.product == null) {
                    val request = CreateProductRequest(
                        harga = tilPrice.editText?.text.toString().toIntOrNull(),
                        nama = tilName.editText?.text.toString().trim(),
                        stock = tilStock.editText?.text.toString().toIntOrNull()
                    )
                    productViewModel.createProduct(request)
                }
                else {
                    val request = UpdateProductRequest(
                        harga = tilPrice.editText?.text.toString().toIntOrNull(),
                        nama = tilName.editText?.text.toString().trim(),
                        stock = tilStock.editText?.text.toString().toIntOrNull()
                    )
                    productViewModel.updateProduct(args.product?.id, request)
                }
            }
        }
    }

    private fun showProducts(product: Product?) {
        product?.let {
            with(binding) {
                tilName.editText?.setText(it.nama)
                tilPrice.editText?.setText(it.harga?.toInt().toString())
                tilStock.editText?.setText(it.stock.toString())

                btnSave.text = getString(R.string.save)
            }
        }
    }

}