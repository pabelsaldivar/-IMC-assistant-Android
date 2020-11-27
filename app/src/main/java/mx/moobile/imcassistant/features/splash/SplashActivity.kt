package mx.moobile.imcassistant.features.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import mx.moobile.imcassistant.base.BaseActivity
import mx.moobile.imcassistant.features.main.MainActivity
import mx.moobile.imcassistant.utils.router.goActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            goActivity<MainActivity>()
            finish()
        }, 3000)
    }
}
