package com.example.lottotest.di

import android.content.Context
import com.example.lottotest.lotteries.DrawInfoApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://api.tipp24.com")
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(DrawInfoApi::class.java)
    }

    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val cacheSize = 5L * 1024 * 1024
        val cache = Cache(get<Context>().cacheDir, cacheSize)

        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .cache(cache)
            .build()
    }
}