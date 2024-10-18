package ar.edu.unlam.mobile.scaffolding

import android.app.Application
import ar.edu.unlam.mobile.scaffolding.data.local.AppContainer
import ar.edu.unlam.mobile.scaffolding.data.local.AppDataContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ScaffoldingApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
