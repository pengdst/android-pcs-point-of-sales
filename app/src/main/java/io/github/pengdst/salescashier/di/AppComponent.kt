package io.github.pengdst.salescashier.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.pengdst.salescashier.data.remote.routes.SalesRoute
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppComponent {
    const val BASE_URL = "https://private-62bcef-pcspointofsales.apiary-mock.com/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSalesRoute(retrofit: Retrofit): SalesRoute = retrofit.create(SalesRoute::class.java)
}