package com.scitalys.ui

import android.app.Application
import timber.log.Timber

public class ApplicationUI : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}