package fuel.hunter.scenes.base.page

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fuel.hunter.ui.ColorPrimary

@Composable
fun LocationIcon(
    modifier: Modifier = Modifier,
    color: Color = ColorPrimary,
) {
    Canvas(modifier = modifier) {
        drawLocation(color)
    }
}

@Preview
@Composable
fun LocationIconPreview() {
    LocationIcon(modifier = Modifier.size(50.dp))
}

fun DrawScope.drawLocation(
    color: Color,
    style: DrawStyle = Fill,
) {
    val path = Path().apply {
        moveTo(0f, size.height / 3f)
        lineTo(size.width, 0f)
        lineTo(size.width * (2f / 3f), size.height)
        lineTo(size.width / 2f, size.height / 2f)
        lineTo(0f, size.height / 3f)
    }

    drawPath(path = path, color = color, style = style)
}