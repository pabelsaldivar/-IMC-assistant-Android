package mx.moobile.imcassistant.features.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_setting.view.*
import mx.moobile.imcassistant.R
import mx.moobile.imcassistant.features.settings.SettingsAdapter.SettingsViewHolder

class SettingsAdapter(): RecyclerView.Adapter<SettingsViewHolder>() {

    private var iSettingsListener: ISettingsListener? = null
    private val settings: MutableList<String> = arrayListOf()

    fun setSettingssListener(iSettingsListener: ISettingsListener): SettingsAdapter {
        return this.apply {
            this.iSettingsListener = iSettingsListener
        }
    }

    fun addSettings(list: List<String>) {
        settings.clear()
        settings.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = settings.size

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bind(settings[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        return SettingsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_setting,
                        parent,
                        false
                )
        )
    }

    inner class SettingsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val vTitle: AppCompatTextView = view.item_title
        private val card: MaterialCardView = view.item_card

        fun bind(option: String) {
            vTitle.text = option
            card.setOnClickListener {
                iSettingsListener?.onItemClicked(option)
            }
            itemView.setOnClickListener {
                iSettingsListener?.onItemClicked(option)
            }
        }
    }

}

interface ISettingsListener {
    fun onItemClicked(option: String)
}
