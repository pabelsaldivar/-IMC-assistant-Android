package mx.moobile.imcassistant.base

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import mx.moobile.imcassistant.utils.Constants.Preferences.MyPref
import mx.moobile.imcassistant.utils.storage.PreferencesHelper

class IMCAssistant : Application() {

    /**
     * Create a instance of the shared preferences
     */
    val preferences by lazy { PreferencesHelper.customPrefs(this, MyPref) }

    companion object {
        lateinit var instance: IMCAssistant
    }

    /**
     * Create and save instance of applications
     */
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        instance = this
    }
}