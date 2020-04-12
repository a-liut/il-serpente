package it.aliut.ilserpente

import android.app.Application
import it.aliut.ilserpente.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class IlSerpenteApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initLogger()
        initKoin()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@IlSerpenteApplication)
            modules(appModule)
        }
    }
}
