package mx.moobile.imcassistant.features.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.toolbar
import mx.moobile.imcassistant.R
import mx.moobile.imcassistant.base.BaseFragment
import mx.moobile.imcassistant.features.about.AboutFragment
import mx.moobile.imcassistant.utils.Constants
import mx.moobile.imcassistant.utils.Constants.huawei_id
import mx.moobile.imcassistant.utils.Constants.packageName
import mx.moobile.imcassistant.utils.router.StackMode

class SettingsFragment: BaseFragment(), ISettingsListener {

    private val settingsAdapter = SettingsAdapter()
    override val layoutId: Int
        get() = R.layout.fragment_settings

    companion object {
        /**
         * Create New instance of fragment
         * @param[bundle] Bundle of the data to send by arguments, it is optional.
         * @return new instance of fragment
         */
        fun newInstance(bundle: Bundle? = null) = SettingsFragment().apply { arguments = bundle }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setEvents()
    }

    fun initViews(){

        cardShop.setOnClickListener {

        }

        cardShop.visibility = GONE

        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        val array = resources.getStringArray(R.array.settings_array)
        recycler_settings.apply {
            adapter = settingsAdapter
            settingsAdapter.addSettings(array.toList())
            settingsAdapter.setSettingssListener(this@SettingsFragment)
            layoutManager = LinearLayoutManager(this.context)
            ViewCompat.setNestedScrollingEnabled(recycler_settings, false)
        }
    }

    fun  setEvents(){

        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }


    }

    override fun onItemClicked(option: String) {
        when(option) {
            "Acerca de" -> {
                loadFragment(AboutFragment.newInstance(),stackMode = StackMode.ADD_STACK, tag = Constants.Fragments.ABOUT_FRAGMENT, container = R.id.container_main)
            }
            "Valorar IMC Assistant" -> {

                showDialogWithAction(title = R.string.ranking_title, message = getString(R.string.ranking_msg), decisive = true, titleNegative = "No, gracias", titlePositive = "Valorar IMC Assistant", action = {
                    val uri: Uri =  if (isHMSAvaliable(context)) {
                        Uri.parse("appmarket://details?id=$packageName")
                    }else{
                        Uri.parse("market://details?id=$packageName")
                    }
                    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                    try {
                        startActivity(goToMarket)
                    } catch (e: ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse(if (isHMSAvaliable(context)) "http://appgallery.cloud.huawei.com/marketshare/app/C$huawei_id" else "http://play.google.com/store/apps/details?id=$packageName")))
                    }
                })
            }
            "Compartir" -> {
//                    val share = Intent.createChooser(Intent().apply {
//                    action = Intent.ACTION_SEND
//                    putExtra(Intent.EXTRA_TEXT, "\"https://itunes.apple.com/app/id1503194339\"")
//                    putExtra(Intent.EXTRA_TITLE, getString(R.string.share_app_message))
//                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//                }, null)
//                startActivity(share)

                Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${getString(R.string.share_app_message)} \"${if (isHMSAvaliable(context)) "http://appgallery.cloud.huawei.com/marketshare/app/C$huawei_id" else "http://play.google.com/store/apps/details?id=$packageName"}\"")
                    type = "text/plain"
                }.also { startActivityForResult(it, 303) }
            }
            else -> {}
        }
    }

}