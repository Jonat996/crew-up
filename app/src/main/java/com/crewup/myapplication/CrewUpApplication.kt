package com.crewup.myapplication

import android.app.Application
import com.google.android.libraries.places.api.Places

class CrewUpApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicializar Google Places
        Places.initialize(applicationContext, getString(R.string.google_maps_key))
    }
}