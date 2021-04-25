package fuel.hunter.scenes.prices

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import fuel.hunter.scenes.prices.pages.NewLocationPage
import fuel.hunter.scenes.prices.pages.PricesPage

@ExperimentalPagerApi
@Composable
fun PricesScene(
    viewModel: PricesSceneVM,
) {
    val locations by viewModel.locations.collectAsState(initial = emptyList())

    if (locations.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = locations.size + 1)
    val selectedPage by snapshotFlow { pagerState.currentPage }.collectAsState(initial = 0)

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (pages, bottomBar) = createRefs()

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .constrainAs(pages) {
                    linkTo(start = parent.start, end = parent.end)
                    width = Dimension.fillToConstraints

                    linkTo(top = parent.top, bottom = bottomBar.top)
                    height = Dimension.fillToConstraints
                }
        ) {
            Log.d("MOX", "called with $it")

            if (it < pagerState.pageCount - 1) {
                PricesPage(prices = viewModel.getPrices(locations[it]))
                return@HorizontalPager
            }

            NewLocationPage(
                modifier = Modifier
                    .padding(
                        top = 53.dp,
                        start = 8.dp,
                        end = 8.dp,
                    )
            )
        }

        BottomBar(
            selectedPageIndex = selectedPage,
            pagesAmount = pagerState.pageCount,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(bottomBar) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}