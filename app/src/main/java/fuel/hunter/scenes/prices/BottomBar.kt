package fuel.hunter.scenes.prices

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fuel.hunter.scenes.base.page.PageIndex
import fuel.hunter.ui.ColorPrimary

sealed class OnBottomBarClick {
    object Settings : OnBottomBarClick()
    object More : OnBottomBarClick()
}

@Preview
@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedPageIndex: Int = 0,
    pagesAmount: Int = 2,
    onClick: (OnBottomBarClick) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        val iconStyle = Modifier
            .padding(16.dp)
            .size(24.dp)

        SettingsIcon(
            modifier = iconStyle.align(Alignment.CenterStart),
            onClick = { onClick(OnBottomBarClick.Settings) },
        )

        PageIndex(
            index = selectedPageIndex,
            amount = pagesAmount,
            selectedColor = ColorPrimary,
            modifier = Modifier.align(Alignment.Center)
        )

        MoreIcon(
            modifier = iconStyle.align(Alignment.CenterEnd),
            onClick = { onClick(OnBottomBarClick.More) },
        )
    }
}