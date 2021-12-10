package io.github.pengdst.salescashier.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.pengdst.salescashier.data.local.prefs.Session
import io.github.pengdst.salescashier.data.local.prefs.SessionHelper
import io.github.pengdst.salescashier.data.remote.routes.SalesRoute
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object AppComponent {
    const val BASE_URL = "https://private-62bcef-pcspointofsales.apiary-mock.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(session: Session) = OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest = chain.request().newBuilder().apply {
            if (session.isLogin()) addHeader("Authorization", "Bearer ${session.token}")
        }.build()
        chain.proceed(newRequest)
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSalesRoute(retrofit: Retrofit): SalesRoute = retrofit.create(SalesRoute::class.java)

    @Provides
    @Singleton
    fun provideSessionHelper(@ApplicationContext context: Context): SessionHelper = SessionHelper
        .newInstance(context)

    @Provides
    @Singleton
    fun provideSession(sessionHelper: SessionHelper): Session = Session(sessionHelper)
}