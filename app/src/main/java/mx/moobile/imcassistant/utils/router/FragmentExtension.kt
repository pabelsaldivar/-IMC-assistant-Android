package mx.moobile.imcassistant.utils.router

import androidx.annotation.AnyRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.view.View
import androidx.annotation.StringRes
import mx.moobile.imcassistant.utils.views.MessageDialog

/**
 * Extension functions for FragmentManager, used launch Fragments
 *
 * @author dD
 */


/**
 * Launch fragment from FragmentManager(v4)
 * @param[fragment] Instance of fragment to show.
 * @param[idContainer] Name id of container where will it show.
 * @param[tag] String for set, it is optional.
 * @param[sharedElements] HashMap of widgets, this is used for create animations
 * @param[hasOptionMenu] If true, the fragment has menu items to contribute.
 * @param[stackMode] Type of stack mode, this will serve for manage the back stack.
 */
fun FragmentManager.loadFragment(fragment: Fragment,
                                                       @AnyRes idContainer: Int,
                                                       tag: String = fragment.toString(),
                                                       sharedElements: HashMap<String, View>? = null,
                                                       hasOptionMenu: Boolean = false,
                                                       stackMode: StackMode = StackMode.INCLUSIVE
) {

    fragment.setHasOptionsMenu(hasOptionMenu)
    val fragmentTransaction = this.beginTransaction()

    when (stackMode) {
        StackMode.INCLUSIVE -> {
            this.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentTransaction.replace(idContainer, fragment, tag)
        }
        StackMode.REPLACE_STACK -> {
            if(this.fragmentAlreadyExist(tag) && getTopFragment(this) == this.findFragmentByTag(tag)) return

            fragmentTransaction.replace(idContainer, fragment, tag)
            fragmentTransaction.addToBackStack(tag)
        }
        StackMode.ADD_STACK -> {
            if (this.fragmentAlreadyExist(tag)) return

            fragmentTransaction.add(idContainer, fragment, tag)
            fragmentTransaction.addToBackStack(tag)
            getTopFragment(this)?.let { fragmentTransaction.hide(it) }
        }
        StackMode.REPLACE -> fragmentTransaction.replace(idContainer, fragment, tag)
    }

    if (sharedElements != null && !sharedElements.isEmpty()) {
        for (element in sharedElements.entries) {
            fragmentTransaction.addSharedElement(element.value, element.key)
        }
    }
    fragmentTransaction.commitAllowingStateLoss()
}

/**
 *
 */
fun FragmentManager.showDialogOnce(dialog: DialogFragment, tag: String){
    if(fragmentAlreadyExist(tag)) return
    dialog.show(this, tag)
}

fun FragmentManager.showDialogFull(dialog: DialogFragment, tag: String){

    val transaction = beginTransaction()
    // For a little polish, specify a transition animation
    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    // To make it fullscreen, use the 'content' root view as the container
    // for the fragment, which is always the root view for the activity
    transaction.add(android.R.id.content, dialog)
            .addToBackStack(null).commit()
}

fun androidx.fragment.app.FragmentManager.fragmentAlreadyExist(tag: String): Boolean {
    return this.findFragmentByTag(tag) != null
}

fun android.app.FragmentManager.fragmentAlreadyExist(tag: String): Boolean {
    return this.findFragmentByTag(tag) != null
}

private fun getTopFragment(fragmentManager: androidx.fragment.app.FragmentManager): androidx.fragment.app.Fragment? {
    fragmentManager.run {
        return when (backStackEntryCount) {
            0 -> null
            else -> findFragmentByTag(getBackStackEntryAt(backStackEntryCount - 1).name)
        }
    }
}


/**
 * This list indicate the manage of BackStack
 * INCLUSIVE -> Clean back stack and set fragment.
 * REPLACE_STACK -> Replace current fragment and add this to back stack.
 * ADD_STACK -> Add current fragment and add this to back stack.
 * REPLACE -> Only replace old by new fragment
 */
enum class StackMode {
    INCLUSIVE, REPLACE_STACK, ADD_STACK, REPLACE
}

fun androidx.fragment.app.FragmentManager.showSimpleDialog(@StringRes titleId: Int, message: String, cancelable: Boolean = true,
                                                           tag: String = "showSimpleDialog"){
    showDialogOnce(MessageDialog.newInstance(titleId, message, false, cancelable), tag)
}

fun androidx.fragment.app.FragmentManager.showSimpleDialog(title: String, message: String, cancelable: Boolean = true,
                                                           tag: String = "showSimpleDialog"){
    showDialogOnce(MessageDialog.newInstance(title, message, false, cancelable), tag)
}


fun androidx.fragment.app.FragmentManager.showDialogWithAction(@StringRes titleId: Int, message: String,
                                                               tag: String = "showDialogWithAction", decisive: Boolean, cancelable: Boolean = true, action: () -> Unit){
    showDialogOnce(MessageDialog.newInstance(titleId, message, decisive,cancelable,null,null, action), tag)
}

fun androidx.fragment.app.FragmentManager.showDialogWithAction(title: String, message: String,
                                                               tag: String = "showDialogWithAction", decisive: Boolean, cancelable: Boolean = true, action: () -> Unit){
    showDialogOnce(MessageDialog.newInstance(title, message, decisive,cancelable,null,null, action), tag)
}


fun androidx.fragment.app.FragmentManager.showDialogWithActionSecundary(title: String, message: String,
                                                                        tag: String = "showDialogWithAction", decisive: Boolean, cancelable: Boolean = true, titleNegative: String? = null, titlePositive: String? = null, action: () -> Unit, actionSecundary: () -> Unit){
    showDialogOnce(MessageDialog.newInstance(title, message, decisive, cancelable,titlePositive, titleNegative, action, actionSecundary), tag)
}

fun androidx.fragment.app.FragmentManager.showDialogWithActionSecundary(@StringRes titleId: Int, message: String,
                                                                        tag: String = "showDialogWithAction", decisive: Boolean, cancelable: Boolean = true, titleNegative: String? = null, titlePositive: String? = null, action: () -> Unit, actionSecundary: () -> Unit){
    showDialogOnce(MessageDialog.newInstance(titleId, message, decisive, cancelable,titlePositive, titleNegative, action, actionSecundary), tag)
}