package com.example.lottotest

import android.app.Application
import com.example.lottotest.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class LotteryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@LotteryApplication)
            modules(networkModule)
        }
    }
}