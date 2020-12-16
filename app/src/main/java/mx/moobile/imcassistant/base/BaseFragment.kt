package mx.moobile.imcassistant.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.huawei.hms.api.HuaweiApiAvailability
import mx.moobile.imcassistant.R
import mx.moobile.imcassistant.utils.RequestError
import mx.moobile.imcassistant.utils.router.*
import mx.moobile.imcassistant.utils.views.ProgressDialog

abstract class BaseFragment: Fragment(),
    RequestError {
    /**
     * Layout resource id for load fragment, this parameter must be override in fragment child
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Inflate view with the layout id override in child fragment, this method is not necessary
     * implement in this fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun showProgressBar() {
        context?.run { ProgressDialog.show(this) }
    }

    fun hideProgressBar() {
        context?.run { ProgressDialog.dismiss() }
    }

    fun hideKeyboard() {
        view?.let {
            val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun loadFragment(fragment: Fragment, stackMode: StackMode = StackMode.REPLACE, tag: String, container: Int) {
        activity?.supportFragmentManager?.loadFragment(fragment,
            container, stackMode = stackMode, tag = tag)
        //(activity as? MainActivity)?.tagfragment = tag
    }

    override fun errorInRequest(messageId: Int, message: String) {
        ProgressDialog.dismiss()
        when {
            messageId != -1 -> showDialog(messageId = messageId)
            message.isNotEmpty() -> showDialog(message = message)
        }
    }

    /**
     * Show a general dialog
     * @param[title] string id for show title, for default the id is [R.string.error_message]]
     * @param[message] string for show message, this is required.
     */
    fun showDialog(@StringRes title: Int = R.string.error_message, message: String){
        fragmentManager?.showSimpleDialog(titleId = title, message = message,tag = "showSimpleDialog")
    }

    /**
     * Show a general dialog
     * @param[title] string id for show title, for default the id is [R.string.error_message]]
     * @param[messageId] string id for show message, this is required.
     */
    fun showDialog(@StringRes title: Int = R.string.error_message, @StringRes messageId: Int){
        fragmentManager?.showSimpleDialog(titleId = title, message = getString(messageId), tag = "showSimpleDialog")
    }

    /**
     * Show a general dialog
     * @param[title] string id for show title, for default the id is [R.string.error_message]]
     * @param[message] string for show message, this is required.
     */
    fun showDialogWithAction(@StringRes title: Int = R.string.error_message, message: String, decisive:Boolean = false, cancelable: Boolean = true, action: () -> Unit = { }) {
        fragmentManager?.showDialogWithAction(titleId = title, message = message,decisive = decisive,cancelable = cancelable, action = action)
    }

    fun showDialogWithAction(title: String, message: String,
                             tag: String = "showDialogWithAction",decisive: Boolean, action: () -> Unit = { }, actionSecundary: () -> Unit = { }){
        fragmentManager?.showDialogWithActionSecundary(title = title, message = message,decisive = decisive, action = action, actionSecundary = actionSecundary)
    }

    fun showDialogWithAction(@StringRes title: Int = R.string.error_message, message: String, decisive:Boolean = false, cancelable: Boolean = true, titleNegative: String? = null, titlePositive: String? = null, action: () -> Unit = { }, actionSecundary: () -> Unit = { }){
        fragmentManager?.showDialogWithActionSecundary(titleId = title, message = message,decisive = decisive, cancelable = cancelable, titleNegative = titleNegative, titlePositive = titlePositive, action = action, actionSecundary = actionSecundary)
    }

    fun showToast(msg: String, isSnackbar: Boolean = true, time: Int = Snackbar.LENGTH_LONG) {
        if (isSnackbar) {
            if (context != null) {
                view?.let {
                    Snackbar.make(it, msg, time)
                        .setBackgroundTint(ContextCompat.getColor(context!!, R.color.primaryLightColor))
                        .show()
                }
            }
        } else {
            Toast.makeText(
                context, msg, when (time) {
                    Snackbar.LENGTH_SHORT -> Toast.LENGTH_SHORT
                    else -> Toast.LENGTH_LONG
                }
            ).show()
        }
    }

    fun isHMSAvaliable(context: Context?): Boolean {
        val result: Int = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context)
        return result == com.huawei.hms.api.ConnectionResult.SUCCESS
    }

}