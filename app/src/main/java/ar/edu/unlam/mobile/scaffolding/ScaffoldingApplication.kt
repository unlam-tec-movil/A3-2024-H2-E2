package ar.edu.unlam.mobile.scaffolding

import android.app.Application
import ar.edu.unlam.mobile.scaffolding.data.local.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ScaffoldingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getDatabase(this)
    }
}
