package io.github.pengdst.salescashier.data.remote.routes

import io.github.pengdst.salescashier.data.remote.models.AuthData
import io.github.pengdst.salescashier.data.remote.models.Product
import io.github.pengdst.salescashier.data.remote.models.Transaction
import io.github.pengdst.salescashier.data.remote.models.TransactionItem
import io.github.pengdst.salescashier.data.remote.requests.CreateItemTransactionRequest
import io.github.pengdst.salescashier.data.remote.requests.CreateProductRequest
import io.github.pengdst.salescashier.data.remote.requests.CreateTransactionRequest
import io.github.pengdst.salescashier.data.remote.requests.UpdateProductRequest
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

    @POST("transaksi")
    suspend fun createTransaction(
        @Body request: CreateTransactionRequest
    ): Response<SalesResponse<Transaction>>

    @POST("transaksi/item")
    suspend fun createTransactionItem(
        @Body request: CreateItemTransactionRequest
    ): Response<SalesResponse<TransactionItem>>

    @PUT("product/{product_id}")
    suspend fun updateProduct(
        @Path("product_id") productId: Int?,
        @Body request: UpdateProductRequest
    ): Response<SalesResponse<Product>>

    @DELETE("product/{product_id}")
    suspend fun deleteProduct(
        @Path("product_id") productId: Int
    ): Response<SalesResponse<List<Product>>>

    @GET("product")
    suspend fun getProducts(): Response<SalesResponse<List<Product>>>

    @GET("transaksi")
    suspend fun getTransactions(): Response<SalesResponse<List<Transaction>>>

    @GET("transaksi/item")
    suspend fun getTransactionItems(): Response<SalesResponse<List<TransactionItem>>>

}