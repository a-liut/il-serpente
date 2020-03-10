package it.aliut.iamdev.ilserpente

import android.app.Application
import timber.log.Timber

class IlSerpenteApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree());
        }
    }
}