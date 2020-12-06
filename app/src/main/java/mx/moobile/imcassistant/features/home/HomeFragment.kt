package mx.moobile.imcassistant.features.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.ads.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import mx.moobile.imcassistant.BuildConfig
import mx.moobile.imcassistant.R
import mx.moobile.imcassistant.base.BaseFragment
import mx.moobile.imcassistant.entity.ImcModel
import mx.moobile.imcassistant.features.main.MainListener
import mx.moobile.imcassistant.features.result.ResultFragment
import mx.moobile.imcassistant.features.settings.SettingsFragment
import mx.moobile.imcassistant.utils.Constants
import mx.moobile.imcassistant.utils.InputFilterDoubleMinMax
import mx.moobile.imcassistant.utils.InputFilterMinMax
import mx.moobile.imcassistant.utils.router.StackMode


class HomeFragment: BaseFragment() {

    var isMen: Boolean? = null
    private lateinit var mInterstitialAd: InterstitialAd
    private var delegate: MainListener? = null

    override val layoutId: Int
        get() = R.layout.fragment_home

    companion object {
        /**
         * Create New instance of fragment
         * @param[bundle] Bundle of the data to send by arguments, it is optional.
         * @return new instance of fragment
         */
        @JvmStatic
        fun newInstance(bundle: Bundle? = null, listener: MainListener? = null) = HomeFragment().apply {
            arguments = bundle
            delegate = listener
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setEvents()
    }

    private fun initViews() {
        addBaners()

        DrawableCompat.setTint(
                imgmen.drawable, ContextCompat.getColor(
                context!!,
                R.color.secondaryTextColor
        )
        )
        DrawableCompat.setTint(
                imgwomen.drawable, ContextCompat.getColor(
                context!!,
                R.color.secondaryTextColor
        )
        )

        tilEdad.editText?.filters = arrayOf(InputFilterMinMax(0, 99))
        tilPeso.editText?.filters = arrayOf(InputFilterDoubleMinMax(0.0, 600.0))
        tilAltura.editText?.filters = arrayOf(InputFilterMinMax(0, 300))

    }

    /**
     * Set events to all widgets
     */
    private fun setEvents() {

        tilEdad.editText?.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (tilEdad.editText?.text?.length == 1 && tilEdad.editText?.text.toString()
                        .trim() == "0"
                ) {
                    tilEdad.editText?.setText("")
                }
            } else {
                if (tilEdad.editText?.text?.length == 0 || tilEdad.editText?.text.toString().toInt() == 0) {
                    tilEdad.editText?.setText("0")
                }
            }
        }

        tilAltura.editText?.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (tilAltura.editText?.text?.length == 1 && tilAltura.editText?.text.toString()
                        .trim() == "0"
                ) {
                    tilAltura.editText?.setText("")
                }
            } else {
                if (tilAltura.editText?.text?.length == 0 || tilAltura.editText?.text.toString().toInt() == 0) {
                    tilAltura.editText?.setText("0")
                }
            }
        }

        tilPeso.editText?.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (tilPeso.editText?.text?.length == 3 && tilPeso.editText?.text.toString()
                        .trim() == "0.0"
                ) {
                    tilPeso.editText?.setText("")
                }
            } else {
                if (tilPeso.editText?.text.toString().toDoubleOrNull() == null) {
                    tilPeso.editText?.setText("0.0")
                }else {
                    if (tilPeso.editText?.text?.length == 0) {
                        tilPeso.editText?.setText("0.0")
                    }else if (tilPeso.editText?.text.toString().toDouble() == 0.0){
                        tilPeso.editText?.setText("0.0")
                    }else if (tilPeso.editText?.text?.last() == '.') {
                        tilPeso.editText?.setText(tilPeso.editText?.text?.dropLast(1))
                    }
                }
            }
        }

        tilPeso.editText?.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                val result = validateData()
                if (result.isNullOrEmpty()) {
                    val data = ImcModel(
                            age = tilEdad.editText?.text.toString().toInt(),
                            height = tilAltura.editText?.text.toString().toDouble(),
                            weight = tilPeso.editText?.text.toString().toDouble(),
                            gender = isMen
                    )
                    loadFragment(
                            ResultFragment.newInstance(data, delegate),
                            stackMode = StackMode.ADD_STACK,
                            tag = Constants.Fragments.RESULT_FRAGMENT,
                            container = R.id.container_main
                    )
                }else{
                    showDialog(message = result)
                }
                true
            }
            false
        }

        toolbar_refresh.setOnClickListener {
            isMen = null
            hideKeyboard()
            tilAltura.clearFocus()
            tilPeso.clearFocus()
            tilEdad.clearFocus()
            DrawableCompat.setTint(
                    imgmen.drawable, ContextCompat.getColor(
                    context!!,
                    R.color.secondaryTextColor
            )
            )
            DrawableCompat.setTint(
                    imgwomen.drawable, ContextCompat.getColor(
                    context!!,
                    R.color.secondaryTextColor
            )
            )
            tilPeso.editText?.setText("0.0")
            tilAltura.editText?.setText("0")
            tilEdad.editText?.setText("0")
        }

        toolbar_config.setOnClickListener {
            loadFragment(
                    SettingsFragment.newInstance(),
                    stackMode = StackMode.ADD_STACK,
                    tag = Constants.Fragments.SETTINGS_FRAGMENT,
                    container = R.id.container_main
            )
        }

        imgmen.setOnClickListener {
            isMen = true
            DrawableCompat.setTint(
                    imgmen.drawable, ContextCompat.getColor(
                    context!!,
                    R.color.primaryColor
            )
            )
            DrawableCompat.setTint(
                    imgwomen.drawable, ContextCompat.getColor(
                    context!!,
                    R.color.secondaryTextColor
            )
            )
        }

        imgwomen.setOnClickListener {
            isMen = false
            DrawableCompat.setTint(
                    imgwomen.drawable, ContextCompat.getColor(
                    context!!,
                    R.color.primaryColor
            )
            )
            DrawableCompat.setTint(
                    imgmen.drawable, ContextCompat.getColor(
                    context!!,
                    R.color.secondaryTextColor
            )
            )
        }

        btn_calculate.setOnClickListener {
            hideKeyboard()
            val result = validateData()
            if (result.isNullOrEmpty()) {
                val data = ImcModel(
                        age = tilEdad.editText?.text.toString().toInt(),
                        height = tilAltura.editText?.text.toString().toDouble(),
                        weight = tilPeso.editText?.text.toString().toDouble(),
                        gender = isMen
                )
                loadFragment(
                        ResultFragment.newInstance(data, delegate),
                        stackMode = StackMode.ADD_STACK,
                        tag = Constants.Fragments.RESULT_FRAGMENT,
                        container = R.id.container_main
                )
            }else{
                showDialog(message = result)
            }
        }
    }

    private fun addBaners() {
        val adRequest = AdRequest.Builder().build()

        val adView = AdView(context)
        adView.adSize = AdSize.LARGE_BANNER
        adView.adUnitId = BuildConfig.BANNER_largeDashboard
        MobileAds.initialize(context) {}
        adViewBanner.addView(adView)
        adView.loadAd(adRequest)

        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError?) {
                super.onAdFailedToLoad(error)
            }
        }


        val adViewParent = AdView(context)
        adViewParent.adSize = AdSize.BANNER
        adViewParent.adUnitId = BuildConfig.BANNER_dashboard
        adViewBannerParent.addView(adViewParent)
        adViewParent.loadAd(adRequest)

        mInterstitialAd = InterstitialAd(context)
        mInterstitialAd.adUnitId = BuildConfig.BANNER_dashboardInterstitial
        mInterstitialAd.loadAd(adRequest)

        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                mInterstitialAd.show()
            }
        }
    }

    private fun validateData(): String? {
//        if (tilEdad.editText?.text!!.equals("0")) {
//            return "La edad es necesaria para calcular el índice de masa corporal"
//        }
        if (tilAltura.editText?.text.toString() == "0") {
            return "La altura es necesaria para calcular el índice de masa corporal"
        }

        if (tilPeso.editText?.text.toString().toDoubleOrNull() == null) {
            return "Introduzca un peso valido para calcular el índice de masa corporal"
        }

        if (tilPeso.editText?.text.toString() == "0.0") {
            return "El peso es necesario para calcular el índice de masa corporal"
        }
        return ""
    }
}