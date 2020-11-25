package fuel.hunter.scenes.base.page

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import fuel.hunter.ui.ColorPrimary

@Composable
fun PageIndex(
    index: Int = 0,
    amount: Int = 5,
    selectedColor: Color = ColorPrimary,
    color: Color = ColorPrimary.copy(alpha = 0.33f),
    iconSize: Dp = 10.dp,
    iconSpace: Dp = iconSize,
    modifier: Modifier = Modifier,
    onDrawPageIndex: DrawScope.(Int, Color) -> Unit = { idx, c ->
        if (idx == 0) drawLocation(c) else drawCircle(c, radius = (iconSize.toPx() / 2) * 0.7f)
    }
) {
    Canvas(
        modifier = modifier
            .size(
                height = iconSize,
                width = iconSize * amount + iconSpace * (amount - 1),
            )
            .clipToBounds()
    ) {
        (0 until amount).onEach {
            val insetLeft = it * (iconSize + iconSpace).toPx()
            val insetRight = (amount - it - 1) * (iconSize + iconSpace).toPx()

            inset(
                left = insetLeft,
                right = insetRight,
                top = 0f,
                bottom = 0f,
            ) {
                onDrawPageIndex(it, if (index == it) selectedColor else color)
            }
        }
    }
}

@Preview
@Composable
fun PageIndexPreview() {
    PageIndex(
        index = 2,
        selectedColor = Color.Cyan,
        amount = 3
    )
}