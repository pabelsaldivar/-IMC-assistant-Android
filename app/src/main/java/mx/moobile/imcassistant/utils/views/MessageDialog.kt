package mx.moobile.imcassistant.utils.views

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import mx.moobile.imcassistant.R
import mx.moobile.imcassistant.utils.Constants.Dialogs.CANCELABLE
import mx.moobile.imcassistant.utils.Constants.Dialogs.DECISIVE
import mx.moobile.imcassistant.utils.Constants.Dialogs.DESCRIPTION_ID
import mx.moobile.imcassistant.utils.Constants.Dialogs.NEGATIVE_TITLE
import mx.moobile.imcassistant.utils.Constants.Dialogs.POSITIVE_TITLE
import mx.moobile.imcassistant.utils.Constants.Dialogs.TITLE
import mx.moobile.imcassistant.utils.Constants.Dialogs.TITLE_ID


class MessageDialog: DialogFragment() {

    companion object {
        var emptyListener: () -> Unit = { }
        var actionListener: () -> Unit = { }
        var actionSecundaryListener: () -> Unit = { }

        /**
         * Create new instance of MessageDialog
         * @param[message] it is the string for show text in view.
         * @return new instance of MessageDialog with arguments stored.
         */
        fun newInstance(@StringRes titleId: Int,
                        message: String, decisive: Boolean, cancelable: Boolean,
                        titlePositive: String? = null,
                        titleNegative: String? = null,
                        action: () -> Unit = emptyListener,
                        actionsecundary: () -> Unit = emptyListener
        ): MessageDialog {
            actionListener = action
            actionSecundaryListener = actionsecundary

            return MessageDialog().apply {
                arguments = Bundle().apply {
                    putInt(TITLE_ID, titleId)
                    putString(DESCRIPTION_ID, message)
                    putBoolean(DECISIVE, decisive)
                    putBoolean(CANCELABLE, cancelable)
                    putString(NEGATIVE_TITLE, titleNegative)
                    putString(POSITIVE_TITLE, titlePositive)
                }
            }
        }

        fun newInstance(title: String,
                        message: String, decisive: Boolean, cancelable: Boolean,
                        titlePositive: String? = null,
                        titleNegative: String? = null,
                        action: () -> Unit = emptyListener,
                        actionsecundary: () -> Unit = emptyListener
        ): MessageDialog {
            actionListener = action
            actionSecundaryListener = actionsecundary

            return MessageDialog().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                    putString(DESCRIPTION_ID, message)
                    putBoolean(DECISIVE, decisive)
                    putBoolean(CANCELABLE, cancelable)
                    putString(POSITIVE_TITLE, titlePositive)
                    putString(NEGATIVE_TITLE, titleNegative)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments?.getString(TITLE)
        val titleId = arguments?.getInt(TITLE_ID)
        val message = arguments?.getString(DESCRIPTION_ID)
        val decisive = arguments?.getBoolean(DECISIVE) ?: true
        val cancelable = arguments?.getBoolean(CANCELABLE) ?: true
        val negativeTitle = arguments?.getString(NEGATIVE_TITLE) ?: getString(R.string.dialog_cancel_button)
        val positiveTitle = arguments?.getString(POSITIVE_TITLE) ?: getString(R.string.dialog_accept_button)

        isCancelable = cancelable

        val builder = AlertDialog.Builder(context!!)

        if (title != null) {
            builder.setTitle(title)
        }

        if (titleId != null) {
            if (titleId != 0) {
                builder.setTitle(titleId)
            }
        }

        if (message != null)
        {
            builder.setMessage(message)
        }

        if(actionListener === emptyListener) {
            builder.setPositiveButton(positiveTitle) { dialog, _ ->
                dialog.dismiss()
            }
        }else {
            if (decisive) {
                builder.setPositiveButton(positiveTitle) { dialog, _ ->
                    dialog.dismiss()
                    actionListener()
                }
                builder.setNegativeButton(negativeTitle) { dialog, _ ->
                    dialog.dismiss()
                    actionSecundaryListener()
                }
            }else {
                builder.setPositiveButton(positiveTitle) { dialog, _ ->
                    dialog.dismiss()
                    actionListener()
                }
            }
        }

        return builder.create()
    }
}