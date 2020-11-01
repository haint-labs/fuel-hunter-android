package fuel.hunter.scenes.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import fuel.hunter.R
import fuel.hunter.databinding.LayoutSettingItemBinding
import fuel.hunter.scenes.base.BaseFragment
import fuel.hunter.scenes.base.ViewLayoutProvider
import fuel.hunter.tools.navigateTo
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

sealed class Preference(
    open val name: String,
    open val description: String
) {
    class Reveal(
        override val name: String,
        override val description: String
    ) : Preference(name, description)

    class Checkbox(
        override val name: String,
        override val description: String,
        val isChecked: Boolean
    ) : Preference(name, description)
}

class PreferencesFragment : BaseFragment<Preference>() {
    override val title = R.string.title_settings

    override val itemDiff = object: DiffUtil.ItemCallback<Preference>() {
        override fun areItemsTheSame(oldItem: Preference, newItem: Preference) = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: Preference, newItem: Preference) = oldItem == newItem
    }

    override val layoutProvider: ViewLayoutProvider = {
        R.layout.layout_setting_item
    }

    override val binder = { view: View, item: Preference ->
        with(LayoutSettingItemBinding.bind(view)) {
            settingTitle.text = item.name
            settingsDescription.text = item.description

            when (item) {
                is Preference.Reveal -> settingToggle.visibility = View.INVISIBLE
                is Preference.Checkbox -> settingToggle.isChecked = item.isChecked
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(adapter) {
            submitList(preferenceItems.keys.toList())

            onItemClick
                .mapNotNull { preferenceItems[it] }
                .onEach { navigateTo(it) }
                .launchIn(lifecycleScope)
        }
    }
}

private val preferenceItems = mapOf(
    Preference.Reveal(
        "NESTE",
        "Atzīmē, kuras uzpildes kompānijas vēlies redzēt sarakstā"
    ) to R.id.settings_to_companies,
    Preference.Reveal(
        "DD",
        "Aktuālais degvielas veids"
    ) to R.id.settings_to_fuel_types,
    Preference.Checkbox(
        "GPS",
        "Izmantot GPS, lai attēlotu lētākās cenas Tavas lokācijas tuvumā",
        true
    ) to null,
    Preference.Checkbox(
        "Paziņojumi",
        "Saņemt paziņojumu telefonā, kad samazinās degvielas cena par 1 centu",
        true
    ) to R.id.settings_to_notification,
    Preference.Reveal(
        "Aplikācijas valoda",
        "Izmaini aplikācijas valodu"
    ) to R.id.settings_to_language,
    Preference.Reveal("Par aplikāciju", "Kā tas strādā") to null
)