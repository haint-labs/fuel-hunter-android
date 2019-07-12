package fuel.hunter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.properties.Delegates
import kotlin.properties.Delegates.observable

fun <T> T.dp(value: Int) where T : View = value * context.resources.displayMetrics.density

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    enum class Style {
        TOP,
        MIDDLE,
        BOTTOM,
        SINGLE
    }

    var style = Style.SINGLE
    var shadowColor = Color.argb(102, 66, 93, 146)

    var radius = dp(4)

    private val shadowPaint = Paint().apply {
        color = shadowColor
        isAntiAlias = true
        maskFilter = BlurMaskFilter(radius, BlurMaskFilter.Blur.OUTER)
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val height = height.toFloat()
        val width = width.toFloat()

        path.reset()

        when (style) {
            Style.TOP -> {
                path.addRoundRect(radius, radius, width - radius, height, radius, radius, Path.Direction.CCW)
                path.addRect(radius, radius, width - 2 * radius, height, Path.Direction.CCW)
                path.addRect(radius, radius + radius, width - radius, height, Path.Direction.CCW)
            }
            Style.MIDDLE -> {
                path.addRect(radius, 0f, width - radius, height, Path.Direction.CCW)
            }
            Style.BOTTOM -> {
                path.addRoundRect(radius, radius, width - radius, height - radius, radius, radius, Path.Direction.CCW)
                path.addRect(radius, 0f, width - radius, height - 2 * radius, Path.Direction.CCW)
            }
            Style.SINGLE -> {
                path.addRoundRect(radius, radius, width - radius, height - radius, radius, radius, Path.Direction.CCW)
            }
        }

        canvas.drawPath(path, shadowPaint)
    }
}

