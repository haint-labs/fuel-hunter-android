package fuel.hunter.scenes.prices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.chrisbanes.accompanist.coil.CoilImage
import fuel.hunter.R
import fuel.hunter.data.Fuel
import fuel.hunter.databinding.FragmentComposeBinding
import fuel.hunter.scenes.base.BaseLayout
import fuel.hunter.scenes.base.GlowingToolbar
import fuel.hunter.scenes.base.Title
import fuel.hunter.scenes.base.list.*
import fuel.hunter.scenes.base.page.LocationIcon
import fuel.hunter.scenes.base.page.PageIndex
import fuel.hunter.scenes.base.rememberToolbarState
import fuel.hunter.tools.di.viewModel
import fuel.hunter.ui.CategoryTextStyle
import fuel.hunter.ui.ColorPrimary
import fuel.hunter.ui.PriceTextStyle
import fuel.hunter.view.decorations.roundIndication

class PricesFragment : Fragment() {
    private val viewModel by viewModel<PricesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentComposeBinding.inflate(inflater, container, false)
        val navController = findNavController()

        view.composeView.setContent {
            PricesScene(viewModel)
        }

        return view.root
    }
}

@Composable
fun PricesScene(
    viewModel: PricesViewModel,
    goToSettings: () -> Unit = {},
) {
    val scrollState = rememberLazyListState()
    val firstItemIndex = scrollState.firstVisibleItemIndex
    val firstItemOffset = scrollState.firstVisibleItemScrollOffset.toFloat()

    val maxAlpha = with(DensityAmbient.current) { 50.dp.toPx() }
    val toolbarState = rememberToolbarState(
        color = ColorPrimary,
        maxAlpha = maxAlpha
    )

    toolbarState.alpha = firstItemOffset.takeIf { firstItemIndex == 0 } ?: maxAlpha

    val items by viewModel.prices.collectAsState()
    val typeDetector = object : ListItemTypeDetector {
        override fun getType(index: Int): ListItemType {
            return when (index) {
                1 -> Top
                2 -> Middle
                3 -> Bottom
                else -> Single
            }
        }
    }

    BaseLayout(
        toolbar = {
            GlowingToolbar(
                title = {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        LocationIcon(
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 4.dp)
                                .size(12.dp)
                                .align(Alignment.Bottom)
                        )

                        Title(text = stringResource(id = R.string.app_name))
                    }
                },
                toolbarState = toolbarState,
            )
        }
    ) {
        Image(
            asset = vectorResource(id = R.drawable.ic_settings),
            modifier = Modifier
                .clickable(
                    onClick = goToSettings,
                    indication = roundIndication(ColorPrimary.copy(alpha = 0.3f)),
                )
                .padding(16.dp)
                .size(24.dp)
                .align(Alignment.BottomStart)
        )

        Canvas(
            modifier = Modifier
                .padding(16.dp)
                .size(24.dp)
                .align(Alignment.BottomEnd)
        ) {
            val lines = (1..3).toList()

            lines.onEach {
                drawLine(
                    color = ColorPrimary,
                    start = Offset(0f, size.height * it / lines.size),
                    end = Offset(size.width, size.height * it / lines.size),
                    strokeWidth = 2.dp.toPx(),
                )
            }
        }

        PageIndex(
            index = 0,
            amount = 5,
            selectedColor = ColorPrimary,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp
                )
                .align(Alignment.BottomCenter)
        )

        val iconModifier = Modifier
            .padding(end = 8.dp)
            .size(33.dp)

        LazyColumnForIndexed(
            items = items,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp),
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
            state = scrollState,
        ) { _, (legacyType, item) ->
            when (item) {
                is Fuel.Category -> Text(
                    text = item.name,
                    style = CategoryTextStyle,
                    modifier = Modifier
                        .padding(
                            top = 28.dp,
                            bottom = 4.dp,
                            start = 8.dp,
                        )
                )
                is Fuel.Price -> ListItem(
                    title = item.title.toUpperCase(),
                    subtitle = item.address,
                    listItemType = typeDetector.getType(legacyType),
                    icon = {
                        when (item.logo) {
                            is Fuel.Logo.Drawable -> Image(
                                asset = imageResource(id = item.logo.id),
                                modifier = iconModifier,
                            )
                            is Fuel.Logo.Url -> CoilImage(
                                data = item.logo.url,
                                modifier = iconModifier,
                            )
                        }
                    },
                    action = {
                        Text(
                            text = item.price.toString(),
                            style = PriceTextStyle,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    },
                )
            }
        }
    }
}