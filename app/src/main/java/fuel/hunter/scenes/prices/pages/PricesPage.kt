package fuel.hunter.scenes.prices.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.CoilImage
import fuel.hunter.R
import fuel.hunter.data.Fuel
import fuel.hunter.scenes.base.BaseLayout
import fuel.hunter.scenes.base.GlowingToolbar
import fuel.hunter.scenes.base.Title
import fuel.hunter.scenes.base.list.*
import fuel.hunter.scenes.base.page.LocationIcon
import fuel.hunter.scenes.base.rememberToolbarState
import fuel.hunter.scenes.prices.FuelTypedItem
import fuel.hunter.scenes.prices.flattenFuelTypes
import fuel.hunter.ui.CategoryTextStyle
import fuel.hunter.ui.ColorPrimary
import fuel.hunter.ui.PriceTextStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Preview
@Composable
fun PricesPage(
    prices: Flow<List<FuelTypedItem>> = dummy
) {
    val scrollState = rememberLazyListState()
    val firstItemIndex = scrollState.firstVisibleItemIndex
    val firstItemOffset = scrollState.firstVisibleItemScrollOffset.toFloat()

    val maxAlpha = with(LocalDensity.current) { 50.dp.toPx() }
    val toolbarState = rememberToolbarState(
        color = ColorPrimary,
        maxAlpha = maxAlpha
    )

    toolbarState.alpha = firstItemOffset.takeIf { firstItemIndex == 0 } ?: maxAlpha

    val items by prices.collectAsState(initial = emptyList())
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

    val iconModifier = Modifier
        .padding(end = 8.dp)
        .size(33.dp)

    BaseLayout(
        toolbar = {
            GlowingToolbar(
                title = {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
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
        },
        children = {
            LazyColumn(
                state = scrollState,
                contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                itemsIndexed(items) { _, (legacyType, item) ->
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
                        is Fuel.Price -> {
                            ListItem(
                                title = item.title.toUpperCase(),
                                subtitle = item.address,
                                listItemType = typeDetector.getType(legacyType),
                                icon = {
                                    when (item.logo) {
                                        is Fuel.Logo.Drawable -> Image(
                                            bitmap = ImageBitmap.imageResource(id = item.logo.id),
                                            contentDescription = null,
                                            modifier = iconModifier,
                                        )
                                        is Fuel.Logo.Url -> CoilImage(
                                            data = item.logo.url,
                                            contentDescription = null,
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
                                onClick = {},
                            )
                        }
                    }
                }
            }
        },
    )
}

private val dummy = flow {
    val items = flattenFuelTypes(
        mapOf(
            Fuel.Category("E95") to listOf(
                Fuel.Price(
                    "NESTE",
                    "Kaivas 50",
                    "E95",
                    1.34f,
                    Fuel.Logo.Drawable(R.drawable.logo_neste),
                ),
                Fuel.Price(
                    "Circle K",
                    "Kaivas 53",
                    "E95",
                    1.23f,
                    Fuel.Logo.Drawable(R.drawable.logo_astarte),
                )
            )
        )
    )

    emit(items)
}