package mx.moobile.imcassistant.utils

import androidx.annotation.StringRes

/**
 * Interface for send error messages in request
 *
 */
interface RequestError {

    /**
     * Implement this function for show error messages
     * @param[messageId] String id of message
     * @param[message] String of message
     */
    fun errorInRequest(@StringRes messageId: Int = -1, message: String = "")

    fun showProgressBar()
}