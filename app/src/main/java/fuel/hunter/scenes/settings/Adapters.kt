package fuel.hunter.scenes.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.R
import fuel.hunter.tools.ui.wrapInShadow
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_BOTTOM
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_MIDDLE
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_TOP
import kotlinx.android.synthetic.main.layout_setting_item.view.*

internal sealed class SettingsItem(
    open val name: String,
    open val description: String
) {
    class Revealable(
        override val name: String,
        override val description: String
    ) : SettingsItem(name, description)

    class Checkbox(
        override val name: String,
        override val description: String,
        val isChecked: Boolean
    ) : SettingsItem(name, description)
}

internal class SettingsAdapter(
    private val items: List<SettingsItem>
) : RecyclerView.Adapter<SettingsItemViewHolder>() {
    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> SHADOW_TOP
        items.lastIndex -> SHADOW_BOTTOM
        else -> SHADOW_MIDDLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsItemViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_setting_item, parent, false)
            .apply { id = View.generateViewId() }

        view = wrapInShadow(parent.context, view, viewType)

        return SettingsItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettingsItemViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

internal class SettingsItemViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {
    fun bind(item: SettingsItem) {
        view.settingTitle.text = item.name
        view.settingsDescription.text = item.description

        when(item) {
            is SettingsItem.Revealable -> view.settingToggle.visibility = View.INVISIBLE
            is SettingsItem.Checkbox -> view.settingToggle.isChecked = item.isChecked
        }
    }
}