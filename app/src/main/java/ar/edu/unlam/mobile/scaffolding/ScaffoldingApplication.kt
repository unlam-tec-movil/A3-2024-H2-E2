package ar.edu.unlam.mobile.scaffolding

import android.app.Application
import android.util.Log
import ar.edu.unlam.mobile.scaffolding.data.local.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ScaffoldingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("debug", "onCreate")
        AppDatabase.getDatabase(this)
    }
}
