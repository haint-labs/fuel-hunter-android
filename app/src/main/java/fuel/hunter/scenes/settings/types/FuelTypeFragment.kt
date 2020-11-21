package fuel.hunter.scenes.settings.types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchConstants.defaultColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import fuel.hunter.R
import fuel.hunter.databinding.FragmentComposeBinding
import fuel.hunter.scenes.base.BaseLayout
import fuel.hunter.scenes.base.GlowingToolbar
import fuel.hunter.scenes.base.list.IndexListItemTypeDetector
import fuel.hunter.scenes.base.list.ListItem
import fuel.hunter.scenes.base.rememberToolbarState
import fuel.hunter.tools.di.viewModel
import fuel.hunter.ui.ColorPrimary
import fuel.hunter.ui.ColorSwitchUnchecked
import fuel.hunter.ui.ItemTextStyle
import kotlinx.coroutines.ExperimentalCoroutinesApi

class FuelTypeFragment : Fragment() {
    private val viewModel by viewModel<FuelTypeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentComposeBinding.inflate(inflater, container, false)
        val navController = findNavController()

        view.composeView.setContent {
            FuelTypeSettingScene(viewModel, navController::navigateUp)
        }

        return view.root
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FuelTypeSettingScene(
    viewModel: FuelTypeViewModel,
    onNavigationClick: () -> Unit = {},
) {
    val items by viewModel.fuelTypes.collectAsState()
    val itemTypeDetector = IndexListItemTypeDetector(items.size)

    BaseLayout(
        toolbar = {
            GlowingToolbar(
                screenTitle = stringResource(id = R.string.title_fuel_type),
                navigationIcon = vectorResource(id = R.drawable.ic_back_arrow),
                toolbarState = rememberToolbarState(
                    color = colorResource(id = R.color.colorPrimary).copy(alpha = 0f)
                ),
                onNavClick = onNavigationClick,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 11.dp)
        ) {
            Text(
                text = "Atzīmē degvielas veidus, kuru cenas\nTev interesē.",
                style = ItemTextStyle,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp)
            )

            items.forEachIndexed { index, item ->
                ListItem(
                    listItemType = itemTypeDetector.getType(index),
                    title = item.name,
                    action = {
                        Switch(
                            checked = item.isChecked,
                            colors = defaultColors(
                                checkedThumbColor = ColorPrimary,
                                uncheckedTrackColor = ColorSwitchUnchecked,
                            ),
                            onCheckedChange = {
                                viewModel.updatePreference(
                                    item.copy(isChecked = it)
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}