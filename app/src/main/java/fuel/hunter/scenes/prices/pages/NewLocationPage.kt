package fuel.hunter.scenes.prices.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fuel.hunter.scenes.base.BaseLayout
import fuel.hunter.scenes.base.GlowingToolbar
import fuel.hunter.scenes.base.list.Single
import fuel.hunter.scenes.base.rememberToolbarState
import fuel.hunter.ui.ColorPrimary
import fuel.hunter.view.decorations.glow

@Preview
@Composable
fun NewLocationPage(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val toolbarState = rememberToolbarState(color = ColorPrimary)
        .also { it.alpha = 0f }

    val glow = Modifier.glow(
        color = ColorPrimary.copy(alpha = 102f / 255f),
        strokeWidth = 1.dp,
        radius = 2.dp,
        path = {
            Single.glow(size, 4.dp.toPx(), 8.dp.toPx())
        }
    )

    val padding = Modifier.padding(16.dp)

    BaseLayout(
        toolbar = {
            GlowingToolbar(
                text = "Jauna vieta",
                toolbarState = toolbarState,
            )
        },
        children = {
            Column(
                modifier = modifier
                    .then(glow)
                    .then(padding)
            ) {
                val center = Modifier.align(Alignment.CenterHorizontally)

                val textStyle = TextStyle(
                    color = ColorPrimary,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                )

                Text(
                    text = "Seko līdzi degvielas cenām\n citur Latvijā",
                    style = textStyle.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = center,
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Parasti uzpildies noteiktā vietā Latvijā?\n\nDod priekšroku noteiktām degvielas uzpildes stacijām?",
                    style = textStyle,
                    modifier = center,
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Uzstādīt",
                    style = textStyle.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier
                        .then(center)
                        .clickable(onClick = onClick),
                )
            }
        },
    )
}
