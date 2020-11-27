package mx.moobile.imcassistant.utils.views

import android.app.Dialog
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import mx.moobile.imcassistant.R

/**
 * Object that showing a progressDialog for each request
 */
object ProgressDialog {
    private var requests = 0
    private var progressDialog: Dialog? = null

    /**
     * Show the progress dialog
     * @param[context] is necessary for create the Dialog
     */
    fun show(context: Context){
        requests ++
        if(progressDialog == null){
            progressDialog = Dialog(context).apply {
                setContentView(R.layout.fragment_loading)
                setCancelable(false)
            }
        }

        progressDialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT)
        progressDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog?.show()
    }

    /**
     * Hide the current progress dialog that is showing
     */
    fun dismiss(){
        if (--requests <= 0){
            progressDialog?.let {
                if(it.isShowing) it.dismiss()
            }
            progressDialog = null
            requests = 0
        }
    }
}