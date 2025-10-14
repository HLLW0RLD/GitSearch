package com.example.gitsearch

import android.app.Application
import com.example.gitsearch.data.di.apiModule
import com.example.gitsearch.data.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                apiModule,
                viewModelModule
            )
        }
    }
}