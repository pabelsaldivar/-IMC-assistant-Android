package mx.moobile.imcassistant.features.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import mx.moobile.imcassistant.R
import mx.moobile.imcassistant.base.BaseActivity
import mx.moobile.imcassistant.features.home.HomeFragment
import mx.moobile.imcassistant.utils.Constants.Fragments.HOME_FRAGMENT

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = ""
        loadFragment(fragment = HomeFragment.newInstance(), tag = HOME_FRAGMENT, container = R.id.container_main)
    }
}