package io.github.pengdst.salescashier.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.pengdst.salescashier.BuildConfig
import io.github.pengdst.salescashier.R
import io.github.pengdst.salescashier.data.local.prefs.Session
import io.github.pengdst.salescashier.data.local.prefs.SessionHelper
import io.github.pengdst.salescashier.data.remote.routes.SalesRoute
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.text.NumberFormat
import java.util.*

@Module
@InstallIn(SingletonComponent::class)
object AppComponent {
    const val BASE_URL = "https://private-62bcef-pcspointofsales.apiary-mock.com/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor() = HttpLoggingInterceptor { message -> Timber.e(message) }.apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(session: Session, loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest = chain.request().newBuilder().apply {
            if (session.isLogin()) addHeader("Authorization", "Bearer ${session.token}")
        }.build()
        chain.proceed(newRequest)
    }.apply {
        if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
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

    @Provides
    @Singleton
    fun provideNumberFormat(): NumberFormat {
        val localeID = Locale("in", "ID")
        return NumberFormat.getCurrencyInstance(localeID)
    }

    @Provides
    @Singleton
    fun provideGlideRequestManager(@ApplicationContext context: Context) = Glide.with(context)
        .applyDefaultRequestOptions(
            RequestOptions().placeholder(R.color.white).error(R.drawable.ic_about)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        )
}