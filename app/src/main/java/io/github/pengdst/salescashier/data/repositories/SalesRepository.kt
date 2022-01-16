package io.github.pengdst.salescashier.data.repositories

import androidx.lifecycle.lifecycleScope
import io.github.pengdst.salescashier.data.local.prefs.Session
import io.github.pengdst.salescashier.data.remote.models.Admin
import io.github.pengdst.salescashier.data.remote.requests.CreateProductRequest
import io.github.pengdst.salescashier.data.remote.requests.UpdateProductRequest
import io.github.pengdst.salescashier.data.remote.responses.ErrorResponse
import io.github.pengdst.salescashier.data.remote.routes.SalesRoute
import io.github.pengdst.salescashier.data.vo.ResultWrapper
import io.github.pengdst.salescashier.utils.longToast
import io.github.pengdst.salescashier.utils.safeApiCall
import io.github.pengdst.salescashier.utils.shortToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SalesRepository @Inject constructor(
    private val salesRoute: SalesRoute,
    private val session: Session
) {

    suspend fun login(email: String, password: String): ResultWrapper<Admin> = safeApiCall {
        val response = salesRoute.login(email, password)
        var message = "Unknown Error"
        if (response.isSuccessful) {
            val responseBody = response.body()

            message = responseBody?.message ?: message
            val loginData = responseBody?.data ?: return@safeApiCall ResultWrapper.Error(data = responseBody?.data?.admin, message = message)

            val admin = loginData.admin ?: return@safeApiCall ResultWrapper.Error(
                data = responseBody.data.admin,
                message = message
            )
            session.saveUser(admin, loginData.token)

            message = responseBody.message ?: "Login Success"
            ResultWrapper.Success(data = session.getAuthUser(), message = message)
        } else {
            val errorBody = ErrorResponse.fromErrorBody(response.errorBody())
            ResultWrapper.Error(data = response.body()?.data?.admin, message = errorBody.message ?: message)
        }
    }

    suspend fun getProducts() = safeApiCall {
        val response = salesRoute.getProducts()
        var message = "Unknown Error"
        if (response.isSuccessful) {
            val responseBody = response.body()

            val products = responseBody?.data ?: emptyList()

            message = responseBody?.message ?: "Get Products Success"
            ResultWrapper.Success(data = products, message = message)
        } else {
            val errorBody = ErrorResponse.fromErrorBody(response.errorBody())
            ResultWrapper.Error(data = response.body()?.data, message = errorBody.message ?: message)
        }
    }

    suspend fun deleteProduct(productId: Int) = safeApiCall {
        val response = salesRoute.deleteProduct(productId)
        var message = "Unknown Error"
        if (response.isSuccessful) {
            val responseBody = response.body()

            val products = responseBody?.data ?: emptyList()

            message = responseBody?.message ?: "Delete Products Success"
            ResultWrapper.Success(data = products, message = message)
        } else {
            val errorBody = ErrorResponse.fromErrorBody(response.errorBody())
            ResultWrapper.Error(data = response.body()?.data, message = errorBody.message ?: message)
        }
    }

    suspend fun createProduct(request: CreateProductRequest) = safeApiCall {
        request.adminId = session.getAuthUser().id
        val response = salesRoute.createProduct(request)
        var message = "Unknown Error"
        if (response.isSuccessful) {
            val responseBody = response.body()

            message = responseBody?.message ?: message
            val product = responseBody?.data ?: return@safeApiCall ResultWrapper.Error(data = responseBody?.data, message = message)

            message = responseBody.message ?: "Create Product Success"
            ResultWrapper.Success(data = product, message = message)
        } else {
            val errorBody = ErrorResponse.fromErrorBody(response.errorBody())
            ResultWrapper.Error(data = response.body()?.data, message = errorBody.message ?: message)
        }
    }

    suspend fun updateProduct(productId: Int?, request: UpdateProductRequest) = safeApiCall {
        request.adminId = session.getAuthUser().id
        val response = salesRoute.updateProduct(productId, request)
        var message = "Unknown Error"
        if (response.isSuccessful) {
            val responseBody = response.body()

            message = responseBody?.message ?: message
            val product = responseBody?.data ?: return@safeApiCall ResultWrapper.Error(data = responseBody?.data, message = message)

            message = responseBody.message ?: "Update Product Success"
            ResultWrapper.Success(data = product, message = message)
        } else {
            val errorBody = ErrorResponse.fromErrorBody(response.errorBody())
            ResultWrapper.Error(data = response.body()?.data, message = errorBody.message ?: message)
        }
    }
}