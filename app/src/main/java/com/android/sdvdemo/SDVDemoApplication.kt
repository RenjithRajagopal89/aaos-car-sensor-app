package com.android.sdvdemo

import android.app.Application
import com.android.data.repository.carApiDataModule
import com.android.sdvdemo.ui.sdvHomeKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SDVDemoApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@SDVDemoApplication)
            modules(
                sdvHomeKoinModule +
                carApiDataModule
            )
        }
    }
}
