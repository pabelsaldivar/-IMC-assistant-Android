package mx.moobile.imcassistant.utils.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * Extension functions for Context and Activity, used launch Activities
 *
 * @author dD
 */


/**
 * Launch activity from Context applying extras if it is required
 * @param[bundle] Bundle of the data to send by extras, it is optional.
 */
inline fun <reified T: Activity> Context.goActivity(bundle: Bundle? = null){
    val intent = Intent(this, T::class.java).apply { bundle?.let { putExtras(it) } }
    startActivity(intent)
}

/**
 * Launch activity from Context applying extras if it is required and flags of FLAG_ACTIVITY_CLEAR_TOP
 * and FLAG_ACTIVITY_SINGLE_TOP
 * @param[bundle] Bundle of the data to send by extras, it is optional.
 */
inline fun <reified T: Activity> Context.goActivityClearStack(bundle: Bundle? = null){
    val intent = Intent(this, T::class.java).apply {
        bundle?.let { putExtras(it) }
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
    startActivity(intent)
}

/**
 * Launch activity for result from Activity applying extras if it is required
 * @param[requestCode] Int value of identifier.
 * @param[bundle] Bundle of the data to send by extras, it is optional.
 */
inline fun <reified T: Activity> Activity.goActivityForResult(requestCode:Int, bundle: Bundle? = null){
    val intent = Intent(this, T::class.java).apply { bundle?.let { putExtras(it) }}
    startActivityForResult(intent,requestCode)
}