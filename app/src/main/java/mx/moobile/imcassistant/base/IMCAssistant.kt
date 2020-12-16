package mx.moobile.imcassistant.base

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.huawei.hms.ads.HwAds
import com.huawei.hms.api.HuaweiApiAvailability
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
        if(isHMSAvaliable(this)) {
            HwAds.init(this)
        }
    }

    fun isHMSAvaliable(context: Context?): Boolean {
        val result: Int = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context)
        return result == com.huawei.hms.api.ConnectionResult.SUCCESS
    }
}