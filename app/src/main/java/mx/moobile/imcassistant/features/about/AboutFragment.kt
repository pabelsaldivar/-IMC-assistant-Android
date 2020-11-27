package mx.moobile.imcassistant.features.about

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_about.*
import mx.moobile.imcassistant.R
import mx.moobile.imcassistant.base.BaseFragment


class AboutFragment: BaseFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_about

    companion object {
        /**
         * Create New instance of fragment
         * @param[bundle] Bundle of the data to send by arguments, it is optional.
         * @return new instance of fragment
         */
        fun newInstance(bundle: Bundle? = null) = AboutFragment().apply { arguments = bundle }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEvents()
    }

    /**
     * Set events to all widgets
     */
    private fun setEvents() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }
}