package fuel.hunter.scenes.prices

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fuel.hunter.R
import fuel.hunter.ui.ColorPrimary

@Preview
@Composable
fun MoreIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    lines: Int = 3,
) {
    Canvas(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(modifier)
    ) {
        val stroke = 2.dp.toPx()

        repeat(lines) {
            drawLine(
                color = ColorPrimary,
                start = Offset(0f, stroke + size.height * it / lines),
                end = Offset(size.width, stroke + size.height * it / lines),
                strokeWidth = stroke,
            )
        }
    }
}

@Preview
@Composable
fun SettingsIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }

    Image(
        painter = painterResource(id = R.drawable.ic_settings),
        contentDescription = null,
        modifier = Modifier
            .clickable(onClick = onClick)
            .indication(interactionSource, LocalIndication.current)
            .then(modifier)
    )
}