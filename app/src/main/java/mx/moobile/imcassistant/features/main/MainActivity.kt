package mx.moobile.imcassistant.features.main

import android.os.Bundle
import android.os.Handler
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import mx.moobile.imcassistant.R
import mx.moobile.imcassistant.base.BaseActivity
import mx.moobile.imcassistant.features.home.HomeFragment
import mx.moobile.imcassistant.utils.Constants.Fragments.HOME_FRAGMENT

class MainActivity : BaseActivity(), MainListener {

//    private var reviewInfo: ReviewInfo? = null
//    var manager: ReviewManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = ""
//        manager = ReviewManagerFactory.create(this)
//        val request = manager?.requestReviewFlow()
//        request?.addOnCompleteListener { request ->
//            reviewInfo = if (request.isSuccessful) {
//                request.result
//            } else {
//                null
//            }
//        }

        loadFragment(fragment = HomeFragment.newInstance(listener = this), tag = HOME_FRAGMENT, container = R.id.container_main)
    }

    override fun onResultSuccess() {
        onBackPressed()
//        Handler().postDelayed({
//            reviewInfo?.let {
//                val flow = manager?.launchReviewFlow(this, it)
//                flow?.addOnCompleteListener {
//                    //Irrespective of the result, the app flow should continue
//                }
//            }
//        }, 3000)
    }
}
interface MainListener {
    fun onResultSuccess()
}