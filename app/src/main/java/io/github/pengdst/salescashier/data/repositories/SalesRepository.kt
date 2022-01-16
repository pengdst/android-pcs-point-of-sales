package io.github.pengdst.salescashier.data.repositories

import io.github.pengdst.salescashier.data.local.prefs.Session
import io.github.pengdst.salescashier.data.remote.models.Admin
import io.github.pengdst.salescashier.data.remote.responses.ErrorResponse
import io.github.pengdst.salescashier.data.remote.routes.SalesRoute
import io.github.pengdst.salescashier.data.vo.ResultWrapper
import io.github.pengdst.salescashier.utils.safeApiCall
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
                data = responseBody?.data?.admin,
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
}