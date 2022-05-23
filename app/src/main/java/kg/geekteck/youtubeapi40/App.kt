package kg.geekteck.youtubeapi40

import android.app.Application
import kg.geekteck.youtubeapi40.di.koinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(koinModules)
        }
    }
}