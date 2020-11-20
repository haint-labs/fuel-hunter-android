package fuel.hunter.view.decorations

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.Region
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class GlowStyle {
    object Single : GlowStyle()
    object Top : GlowStyle()
    object Middle : GlowStyle()
    object Bottom : GlowStyle()
}

fun singleItem(size: Size, radius: Float) = Path().apply {
    addRoundRect(
        RoundRect(
            left = radius,
            top = radius,
            right = size.width - radius,
            bottom = size.height - radius,
            cornerRadius = CornerRadius(radius, radius),
        )
    )

    addRect(
        Rect(
            left = radius,
            top = radius,
            right = size.width - 2 * radius,
            bottom = size.height - 2 * radius,
        )
    )
}

fun topItem(size: Size, radius: Float) = Path().apply {
    addRoundRect(
        RoundRect(
            left = radius,
            top = radius,
            right = size.width - radius,
            bottom = size.height,
            cornerRadius = CornerRadius(radius, radius),
        )
    )

    addRect(
        Rect(
            left = radius,
            top = radius,
            right = size.width - 2 * radius,
            bottom = size.height,
        )
    )

    addRect(
        Rect(
            left = radius,
            top = 2 * radius,
            right = size.width - radius,
            bottom = size.height,
        )
    )
}

fun middleItem(size: Size, radius: Float) = Path().apply {
    addRect(
        Rect(
            left = radius,
            top = 0f,
            right = size.width - radius,
            bottom = size.height,
        )
    )
}

fun bottomItem(size: Size, radius: Float) = Path().apply {
    addRoundRect(
        RoundRect(
            left = radius,
            top = radius,
            right = size.width - radius,
            bottom = size.height - radius,
            cornerRadius = CornerRadius(radius, radius),
        )
    )

    addRect(
        Rect(
            left = radius,
            top = 0f,
            right = size.width - radius,
            bottom = size.height - 2 * radius,
        )
    )
}

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

        it.nativeCanvas.clipPath(nativePath, Region.Op.DIFFERENCE)
        it.nativeCanvas.drawPath(nativePath, outerBlurPaint)
    }
}