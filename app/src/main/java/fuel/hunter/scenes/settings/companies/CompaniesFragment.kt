package fuel.hunter.scenes.settings.companies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.chrisbanes.accompanist.coil.CoilImage
import fuel.hunter.R
import fuel.hunter.databinding.FragmentComposeBinding
import fuel.hunter.scenes.base.BaseLayout
import fuel.hunter.scenes.base.GlowingToolbar
import fuel.hunter.scenes.base.list.IndexListItemTypeDetector
import fuel.hunter.scenes.base.list.ListItem
import fuel.hunter.scenes.base.rememberToolbarState
import fuel.hunter.scenes.settings.Fuel
import fuel.hunter.tools.di.viewModel
import fuel.hunter.ui.ColorPrimary
import fuel.hunter.ui.ColorSwitchUnchecked
import fuel.hunter.ui.ItemTextStyle
import kotlinx.coroutines.ExperimentalCoroutinesApi

class CompaniesFragment : Fragment() {
    private val viewModel by viewModel<CompaniesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentComposeBinding.inflate(inflater, container, false)
        val navController = findNavController()

        view.composeView.setContent {
            CompaniesSettingScene(viewModel, navController::navigateUp)
        }

        return view.root
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun CompaniesSettingScene(
    viewModel: CompaniesViewModel,
    onNavigationClick: () -> Unit = {},
) {
    val items by viewModel.companies.collectAsState()
    val itemTypeDetector = IndexListItemTypeDetector(items.size)

    val scrollState = rememberScrollState(0)
    val toolbarState = rememberToolbarState(
        color = colorResource(id = R.color.colorPrimary),
        maxAlpha = with(LocalDensity.current) { 50.dp.toPx() }
    )

    toolbarState.alpha = scrollState.value.toFloat()

    BaseLayout(
        toolbar = {
            GlowingToolbar(
                toolbarState = toolbarState,
                text = stringResource(id = R.string.title_companies),
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = null,
                    )
                },
                onNavigationClick = onNavigationClick,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxWidth()
                .padding(start = 11.dp, end = 11.dp, bottom = 11.dp)
        ) {
            Text(
                text = "Atzīmē kuras uzpildes kompānijas\nvēlies redzēt sarakstā.",
                style = ItemTextStyle,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp)
            )

            val iconModifier = Modifier
                .size(24.dp, 24.dp)
                .padding(end = 6.dp)

            items.forEachIndexed { index, item ->
                ListItem(
                    listItemType = itemTypeDetector.getType(index),
                    title = item.name,
                    subtitle = item.description,
                    icon = {
                        when (item.logo) {
                            is Fuel.Logo.Drawable -> Image(
                                painter = painterResource(id = item.logo.id),
                                contentDescription = null,
                                modifier = iconModifier
                            )
                            is Fuel.Logo.Url -> CoilImage(
                                data = item.logo.url,
                                contentDescription = null,
                                modifier = iconModifier
                            )
                        }
                    },
                    action = {
                        Switch(
                            checked = item.isChecked,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ColorPrimary,
                                uncheckedTrackColor = ColorSwitchUnchecked,
                            ),
                            onCheckedChange = {
                                viewModel.updateCompaniesPreference(
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