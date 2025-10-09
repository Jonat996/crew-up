package com.crewup.myapplication

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

class CrewUpApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inicializar Facebook SDK
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }
}