package mx.moobile.imcassistant.features.result

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.*
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.gson.Gson
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.banner.BannerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.android.synthetic.main.fragment_result.adViewBannerParent
import kotlinx.android.synthetic.main.fragment_result.btn_calculate
import mx.moobile.imcassistant.BuildConfig
import mx.moobile.imcassistant.R
import mx.moobile.imcassistant.base.BaseFragment
import mx.moobile.imcassistant.entity.ImcModel
import mx.moobile.imcassistant.features.main.MainListener
import mx.moobile.imcassistant.utils.extensions.loadBitmap
import kotlin.math.ceil


private const val ARG_IMC = "ARG_IMC"

class ResultFragment: BaseFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_result

    private lateinit var data: ImcModel
    private lateinit var mInterstitialAd: InterstitialAd
    private var delegate: MainListener? = null
    private lateinit var interstitialAd: com.huawei.hms.ads.InterstitialAd

    companion object {
        /**
         * Create New instance of fragment
         * @param[bundle] Bundle of the data to send by arguments, it is optional.
         * @return new instance of fragment
         */
        @JvmStatic
        fun newInstance(imc: ImcModel, listener: MainListener? = null) =
                ResultFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_IMC, Gson().toJson(imc))
                    }
                    delegate = listener
                }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setEvents()
    }

    private fun initViews() {
        arguments.let {
            if (it != null) {
                data = Gson().fromJson(it.getString(ARG_IMC), ImcModel::class.java)
            }
        }
        txtIMC.text = "IMC: ${data.resultFormatted()}"
        txtSecundaryIMC.text = data.resultFormatted()
        txtHeight.text = data.height.toInt().toString()
        txtWeight.text = data.weight.toString()

        if (data.age == 0) {
            txtAge.text = "N/A"
            txtAgeLabel.visibility = View.GONE
        }else{
            txtAge.text = data.age.toString()
        }

        if (data.gender == null) {
            imgGender.setImageBitmap(null)
        }else{
            imgGender.loadBitmap(if (data.gender!!) R.drawable.men else R.drawable.women)
        }
        if (data.level() < 7) {
            val a = (data.level() * 12).toDouble()/8
            val b = ceil(a).toInt()
            val grapResult = b - 1
            val items = getAllViews(indicatorBarLinearLayout)
            for (item in items!!) {
                if (grapResult <= item.tag.toString().toInt()) {
                   if (item is AppCompatImageView) {
                       item.loadBitmap(R.drawable.imc_shape_placeholder)
                   }
                }
            }
        }

        txtLevel.text = data.levelName()
        txtCat.text = data.levelSortName()

        txtKG.text = data.desiredWeight()
        txtRangeIMC.text = data.levelIMCRange()
        txtRangeWeight.text = data.weightRangeFormatted()


        val linear = view?.findViewWithTag<LinearLayout>("level${data.level()}Linearlayout")
        val items = getAllViews(linear!!)
        for (item in items!!) {
            if (item is AppCompatTextView) {
                item.setTextColor(ContextCompat.getColor(context!!, R.color.primaryColor))
            }
        }

        changeColors()

        if (isHMSAvaliable(context)) {
            addBanershw()
        }else {
            addBaners()
        }
    }

    /**
     * Set events to all widgets
     */
    private fun setEvents() {
        btn_calculate.setOnClickListener {
            delegate?.onResultSuccess()
        }

        toolbar_close.setOnClickListener {
            delegate?.onResultSuccess()
        }
    }

    fun getAllViews(layout: ViewGroup): List<View>? {
        val views: MutableList<View> = ArrayList()
        for (i in 0 until layout.childCount) {
            views.add(layout.getChildAt(i))
        }
        return views
    }

    fun changeColors() {
        var indicatorColor =  when(data.level()) {
            0 -> ContextCompat.getColor(context!!, R.color.colorlevel0)
            1 -> ContextCompat.getColor(context!!, R.color.colorlevel1)
            2 -> ContextCompat.getColor(context!!, R.color.colorlevel2)
            3 -> ContextCompat.getColor(context!!, R.color.colorlevel3)
            4 -> ContextCompat.getColor(context!!, R.color.colorlevel4)
            5 -> ContextCompat.getColor(context!!, R.color.colorlevel5)
            6 -> ContextCompat.getColor(context!!, R.color.colorlevel6)
            7 -> ContextCompat.getColor(context!!, R.color.colorlevel7)
            else -> ContextCompat.getColor(context!!, R.color.colorlevel3)
        }

        txtIMC.setTextColor(indicatorColor)
    }


    private fun addBaners() {
        val adRequest = AdRequest.Builder().build()

        val adViewParent = AdView(context)
        adViewParent.adSize = AdSize.BANNER
        adViewParent.adUnitId = BuildConfig.BANNER_calculator
        adViewBannerParent.addView(adViewParent)
        adViewParent.loadAd(adRequest)

        mInterstitialAd = InterstitialAd(context)
        mInterstitialAd.adUnitId = BuildConfig.BANNER_interstitial
        mInterstitialAd.loadAd(adRequest)

        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                mInterstitialAd.show()
            }
        }
    }

    private fun addBanershw() {
        val adParam = AdParam.Builder().build()
        val adViewParent = BannerView(context)
        adViewParent.adId = BuildConfig.HW_BANNER_calculator
        adViewParent.bannerAdSize = BannerAdSize.BANNER_SIZE_320_100
        adViewBannerParent.addView(adViewParent)
        adViewParent.loadAd(adParam)

        interstitialAd = com.huawei.hms.ads.InterstitialAd(context)
        interstitialAd.adId = BuildConfig.HW_BANNER_interstitial
        interstitialAd.loadAd(adParam)

        interstitialAd.adListener = object : com.huawei.hms.ads.AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                interstitialAd.show()
            }
        }
    }
}