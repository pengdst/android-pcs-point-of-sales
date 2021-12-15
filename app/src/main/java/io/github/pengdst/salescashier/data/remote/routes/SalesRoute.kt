package io.github.pengdst.salescashier.data.remote.routes

import io.github.pengdst.salescashier.data.remote.models.AuthData
import io.github.pengdst.salescashier.data.remote.models.Product
import io.github.pengdst.salescashier.data.remote.requests.CreateProductRequest
import io.github.pengdst.salescashier.data.remote.responses.SalesResponse
import retrofit2.Response
import retrofit2.http.*

interface SalesRoute {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Response<SalesResponse<AuthData>>

    @POST("product")
    suspend fun createProduct(
        @Body request: CreateProductRequest
    ): Response<SalesResponse<Product>>

    @GET("product")
    suspend fun getProducts(): Response<SalesResponse<List<Product>>>

}