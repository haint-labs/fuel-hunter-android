package fuel.hunter.scenes.settings

import android.view.View
import fuel.hunter.R
import fuel.hunter.scenes.base.BaseFragment
import fuel.hunter.scenes.base.ItemClickListener
import fuel.hunter.tools.navigateTo
import kotlinx.android.synthetic.main.layout_setting_item.view.*

sealed class SettingsItem(
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

val settingsItems = mapOf(
    SettingsItem.Revealable(
        "NESTE",
        "Atzīmē, kuras uzpildes kompānijas vēlies redzēt sarakstā"
    ) to R.id.settings_to_companies,
    SettingsItem.Revealable("DD", "Aktuālais degvielas veids") to null,
    SettingsItem.Checkbox(
        "GPS",
        "Izmantot GPS, lai attēlotu lētākās cenas Tavas lokācijas tuvumā",
        true
    ) to null,
    SettingsItem.Checkbox(
        "Paziņojumi",
        "Saņemt paziņojumu telefonā, kad samazinās degvielas cena par 1 centu",
        true
    ) to null,
    SettingsItem.Revealable(
        "Aplikācijas valoda",
        "Izmaini aplikācijas valodu"
    ) to R.id.settings_to_language,
    SettingsItem.Revealable("Par aplikāciju", "Kā tas strādā") to null
)

class SettingsFragment : BaseFragment<SettingsItem>() {
    override val title = R.string.title_settings
    override val items = settingsItems.keys.toList()

    override val layoutProvider = { _: Int ->
        R.layout.layout_setting_item
    }

    override val binder = { view: View, item: SettingsItem ->
        with(view) {
            settingTitle.text = item.name
            settingsDescription.text = item.description

            when (item) {
                is SettingsItem.Revealable -> settingToggle.visibility = View.INVISIBLE
                is SettingsItem.Checkbox -> settingToggle.isChecked = item.isChecked
            }
        }
    }

    override var onClick: ItemClickListener<SettingsItem> = { item: SettingsItem ->
        settingsItems[item]?.let { navigateTo(it) }
    }
}
