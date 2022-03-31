package com.sutrix.uatest

import android.app.Application
import com.sutrix.uatest.di.module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class UaTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@UaTestApplication)
            modules(
                module
            )
        }
    }
}