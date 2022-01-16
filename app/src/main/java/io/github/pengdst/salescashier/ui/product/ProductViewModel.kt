package io.github.pengdst.salescashier.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.pengdst.salescashier.data.remote.models.Product
import io.github.pengdst.salescashier.data.repositories.SalesRepository
import io.github.pengdst.salescashier.data.vo.ResultWrapper
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductViewModel @Inject constructor(
    private val repository: SalesRepository
) : ViewModel() {

    private val _productsViewModel = MutableLiveData<State>()
    val productsViewModel: LiveData<State> get() = _productsViewModel

    fun getProducts() {
        viewModelScope.launch {
            _productsViewModel.postValue(State.Loading)
            viewModelScope.launch {
                when(val res = repository.getProducts()) {
                    is ResultWrapper.Error -> _productsViewModel.value = State.Success(res.data ?: emptyList(), res.message)
                    is ResultWrapper.Success -> _productsViewModel.value = State.Failed(res.message)
                }
            }
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            _productsViewModel.postValue(State.Loading)
            viewModelScope.launch {
                when(val res = repository.deleteProduct(productId)) {
                    is ResultWrapper.Error -> _productsViewModel.value = State.SuccessDelete(res.data ?: emptyList(), res.message)
                    is ResultWrapper.Success -> _productsViewModel.value = State.Failed(res.message)
                }
            }
        }
    }

    sealed class State {
        data class Success(val data: List<Product>, val message: String): State()
        data class SuccessDelete(val data: List<Product>, val message: String): State()
        data class Failed(val message: String): State()
        object Loading: State()
    }

}