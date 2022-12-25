package com.isthive.ist.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.isthive.ist.app.helper.LocaleHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
internal class MyApplication : Application() {
    private val application: MyApplication? = null

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun getApplication(): MyApplication? {
        return application
    }

    /**
     * override to change local so that language can be changed from android device nougat and above
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.setLocale(this, LocaleHelper.defaultLanguage)
    }
}