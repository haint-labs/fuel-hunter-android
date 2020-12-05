package fuel.hunter.scenes.settings

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchConstants
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.DiffUtil
import androidx.ui.tooling.preview.Preview
import fuel.hunter.R
import fuel.hunter.databinding.LayoutSettingItemBinding
import fuel.hunter.scenes.base.*
import fuel.hunter.scenes.base.list.IndexListItemTypeDetector
import fuel.hunter.scenes.base.list.ListItem
import fuel.hunter.ui.ColorPrimary
import fuel.hunter.ui.ColorSwitchUnchecked

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

    override val itemDiff = object : DiffUtil.ItemCallback<Preference>() {
        override fun areItemsTheSame(oldItem: Preference, newItem: Preference) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Preference, newItem: Preference) =
            oldItem == newItem
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

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        with(adapter) {
//            submitList(preferenceItems.keys.toList())
//
//            onItemClick
//                .mapNotNull { preferenceItems[it] }
//                .onEach { navigateTo(it) }
//                .launchIn(lifecycleScope)
//        }
//    }
}

interface NavActions {
    fun back()
    fun toFuelTypes()
    fun toNotifications()
    fun toLanguage()
}

fun noopNavActions(): NavActions {
    return object : NavActions {
        override fun back() {}
        override fun toFuelTypes() {}
        override fun toNotifications() {}
        override fun toLanguage() {}
    }
}

@Preview
@Composable
fun SettingsScene(
    navActions: NavActions = noopNavActions(),
    onNavigationClick: () -> Unit = {},
) {
    val toolbarState = rememberToolbarState(
        color = ColorPrimary,
    )
    toolbarState.alpha = 0f

    val itemTypeDetector = IndexListItemTypeDetector(preferenceItems.size)

    BaseLayout(
        toolbar = {
            GlowingToolbar(
                toolbarState = toolbarState,
                text = stringResource(id = R.string.title_settings),
                navigationIcon = {
                    Image(asset = vectorResource(id = R.drawable.ic_back_arrow))
                },
                onNavigationClick = onNavigationClick,
            )
        }
    ) {
        ScrollableColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(11.dp)
        ) {
            preferenceItems.forEachIndexed { index, (it, destination) ->
                ListItem(
                    listItemType = itemTypeDetector.getType(index),
                    title = it.name,
                    subtitle = it.description,
                    action = {
                        when (it) {
                            is Preference.Checkbox -> {
                                Switch(
                                    checked = it.isChecked,
                                    colors = SwitchConstants.defaultColors(
                                        checkedThumbColor = ColorPrimary,
                                        uncheckedTrackColor = ColorSwitchUnchecked,
                                    ),
                                    onCheckedChange = {}
                                )
                            }
                            is Preference.Reveal -> {
                            }
                        }
                    },
                    onClick = {
                        when (destination) {
                            R.id.settings_to_fuel_types -> navActions.toFuelTypes()
                            R.id.settings_to_notification -> navActions.toNotifications()
                            R.id.settings_to_language -> navActions.toLanguage()
                        }
                    }
                )
            }
        }
    }
}

private val preferenceItems = listOf(
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