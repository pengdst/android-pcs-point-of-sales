package io.github.pengdst.salescashier.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.pengdst.salescashier.data.remote.models.Transaction
import io.github.pengdst.salescashier.data.remote.requests.CreateItemTransactionRequest
import io.github.pengdst.salescashier.data.remote.requests.CreateTransactionRequest
import io.github.pengdst.salescashier.data.repositories.SalesRepository
import io.github.pengdst.salescashier.data.vo.ResultWrapper
import kotlinx.coroutines.launch
import javax.inject.Inject
import io.github.pengdst.salescashier.data.remote.models.TransactionItem as TransactionDomain

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: SalesRepository
) : ViewModel() {

    private val _transactionViewModel = MutableLiveData<State>()
    val productsViewModel: LiveData<State> get() = _transactionViewModel

    fun payTransaction(request: CreateTransactionRequest, productTransactions: List<TransactionItem>) {
        _transactionViewModel.postValue(State.Loading)
        viewModelScope.launch {
            when(val res = repository.payTransaction(request)) {
                is ResultWrapper.Success -> {
                    productTransactions.forEach {
                        val itemTransactionRequest = CreateItemTransactionRequest(
                            productId = it.product.id,
                            harga = it.product.harga,
                            qty = it.amount,
                            transaksiId = res.data.id
                        )
                        createTransactionItem(itemTransactionRequest)
                    }
                    _transactionViewModel.value = State.SuccessPayTransaction(res.data, res.message)
                }

                is ResultWrapper.Error -> _transactionViewModel.value = State.Failed(res.message)
            }
        }
    }

    private suspend fun createTransactionItem(request: CreateItemTransactionRequest) {
        _transactionViewModel.postValue(State.Loading)
        when(val res = repository.createTransactionItem(request)) {
            is ResultWrapper.Success -> _transactionViewModel.value = State.SuccessCreateItem(res.data, res.message)
            is ResultWrapper.Error -> _transactionViewModel.value = State.Failed(res.message)
        }
    }

    sealed class State {
        data class SuccessPayTransaction(val data: Transaction, val message: String): State()
        data class SuccessCreateItem(val data: TransactionDomain, val message: String): State()
        data class Failed(val message: String): State()
        object Loading: State()
    }

}