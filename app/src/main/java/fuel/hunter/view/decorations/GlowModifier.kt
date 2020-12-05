package fuel.hunter.view.decorations

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.Region
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.glow(
    radius: Dp = 10.dp,
    strokeWidth: Dp = 3.dp,
    color: Color = Color.Black,
    path: DrawScope.() -> Path,
) = drawBehind {
    drawIntoCanvas {
        val nativePath = path()
            .asAndroidPath()

        val outerBlurPaint = Paint()
            .apply {
                isAntiAlias = true

                style = Paint.Style.STROKE
                this.strokeWidth = strokeWidth.toPx()

                this.color = color.toArgb()
                alpha = (color.alpha * 255).toInt()

                maskFilter = BlurMaskFilter(radius.toPx(), BlurMaskFilter.Blur.NORMAL)
            }

        with(it.nativeCanvas) {
            save()
            clipPath(nativePath, Region.Op.DIFFERENCE)
            drawPath(nativePath, outerBlurPaint)
            restore()
        }
    }
}