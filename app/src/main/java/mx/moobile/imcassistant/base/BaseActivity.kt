package mx.moobile.imcassistant.base

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import mx.moobile.imcassistant.R
import mx.moobile.imcassistant.utils.RequestError
import mx.moobile.imcassistant.utils.router.*
import mx.moobile.imcassistant.utils.views.ProgressDialog

abstract class BaseActivity: AppCompatActivity(), RequestError {

    override fun showProgressBar() {
        if (!this@BaseActivity.isDestroyed)
            ProgressDialog.show(this)
    }

    override fun errorInRequest(messageId: Int, message: String) {
        ProgressDialog.dismiss()
        when {
            messageId != -1 -> showDialog(messageId = messageId)
            message.isNotEmpty() -> showDialog(message = message)
        }
    }


    fun loadFragment(fragment: androidx.fragment.app.Fragment, stackMode: StackMode = StackMode.REPLACE, tag: String, container: Int) {
        supportFragmentManager?.run {
            if (backStackEntryCount > 0) {
                popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
        supportFragmentManager.loadFragment(fragment,
            container, stackMode = stackMode, tag = tag)
        //(this as? MainActivity)?.tagfragment = tag

    }

    /**
     * Show a general dialog
     * @param[title] string id for show title, for default the id is [R.string.error_message]]
     * @param[message] string for show message, this is required.
     */
    fun showDialog(@StringRes title: Int = R.string.error_message, message: String, cancelable: Boolean = true) {
        supportFragmentManager.showSimpleDialog(title, message, cancelable, "showSimpleDialog")
    }

    /**
     * Show a general dialog
     * @param[title] string id for show title, for default the id is [R.string.error_message]]
     * @param[messageId] string id for show message, this is required.
     */
    fun showDialog(@StringRes title: Int = R.string.error_message, @StringRes messageId: Int, cancelable: Boolean = true) {
        supportFragmentManager.showSimpleDialog(title, getString(messageId), cancelable, "showSimpleDialog")
    }

    /**
     * Show a general dialog
     * @param[title] string id for show title, for default the id is [R.string.error_message]]
     * @param[message] string for show message, this is required.
     */
    fun showDialogWithAction(@StringRes title: Int = R.string.error_message, message: String, decisive:Boolean = false, cancelable: Boolean = true, action: () -> Unit = { }) {
        supportFragmentManager.showDialogWithAction(titleId = title, message = message,decisive = decisive,cancelable = cancelable, action = action)
    }

    fun showDialogWithAction(title: String, message: String,
                             tag: String = "showDialogWithAction",decisive: Boolean, action: () -> Unit = { }, actionSecundary: () -> Unit = { }){
        supportFragmentManager.showDialogWithActionSecundary(title = title, message = message,decisive = decisive, action = action, actionSecundary = actionSecundary)
    }

    fun showDialogWithAction(title: String, message: String,
                             tag: String = "showDialogWithAction",decisive: Boolean, cancelable: Boolean = true, action: () -> Unit = { }, actionSecundary: () -> Unit = { }){
        supportFragmentManager.showDialogWithActionSecundary(title = title, message = message,decisive = decisive,cancelable = cancelable, action = action, actionSecundary = actionSecundary)
    }

    fun showDialogWithAction(@StringRes title: Int = R.string.error_message, message: String, decisive:Boolean = false, cancelable: Boolean = true, action: () -> Unit = { }, actionSecundary: () -> Unit = { }){
        supportFragmentManager.showDialogWithActionSecundary(titleId = title, message = message,decisive = decisive, cancelable = cancelable, action = action, actionSecundary = actionSecundary)
    }

    fun showDialogWithAction(@StringRes title: Int = R.string.error_message, message: String, decisive:Boolean = false, cancelable: Boolean = true, titleNegative: String? = null, action: () -> Unit = { }, actionSecundary: () -> Unit = { }){
        supportFragmentManager.showDialogWithActionSecundary(titleId = title, message = message,decisive = decisive, cancelable = cancelable, titleNegative = titleNegative, action = action, actionSecundary = actionSecundary)
    }

    @SuppressLint("WrongConstant")
    fun showToast(msg:String, isSnackbar: Boolean = true, time: Int = Snackbar.LENGTH_LONG) {
        if (isSnackbar) {
            findViewById<View>(android.R.id.content)?.let { Snackbar.make(it, msg, time).show() }
        }else{
            Toast.makeText(this, msg, time).show()
        }
    }
}