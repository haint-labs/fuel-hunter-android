package fuel.hunter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

private fun <T> T.dp(value: Int) where T : View = value * context.resources.displayMetrics.density

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val radius = dp(8)
    private val margin = radius

    private val shadowColor = Color.argb(102, 66, 93, 146)

    private val shadowPaint = Paint().apply {
        color = shadowColor
        isAntiAlias = true
        maskFilter = BlurMaskFilter(radius, BlurMaskFilter.Blur.OUTER)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val height = height.toFloat()
        val width = width.toFloat()

        val path = Path()

        path.addRoundRect(margin, margin, width - radius, height, radius, radius, Path.Direction.CCW)
        path.addRect(margin, margin, width - 2 * radius, height, Path.Direction.CCW)
        path.addRect(margin, margin + radius, width - radius, height, Path.Direction.CCW)

        canvas.drawPath(path, shadowPaint)
    }
}