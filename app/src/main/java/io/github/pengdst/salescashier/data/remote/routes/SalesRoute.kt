package io.github.pengdst.salescashier.data.remote.routes

import io.github.pengdst.salescashier.data.remote.responses.AuthData
import io.github.pengdst.salescashier.data.remote.responses.SalesResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.POST

interface SalesRoute {

    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Response<SalesResponse<AuthData>>

}