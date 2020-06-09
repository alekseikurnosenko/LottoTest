package com.example.lottotest.di

import android.content.Context
import com.example.lottotest.lotteries.DrawInfoApi
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://api.tipp24.com")
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
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