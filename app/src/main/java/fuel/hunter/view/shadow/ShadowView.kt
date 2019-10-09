package fuel.hunter.view.shadow

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IntDef
import fuel.hunter.R
import fuel.hunter.extensions.dp

class ShadowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val SHADOW_SINGLE = 0
        const val SHADOW_TOP = 1
        const val SHADOW_MIDDLE = 2
        const val SHADOW_BOTTOM = 3
        const val SHADOW_ALL = 4

        @IntDef(SHADOW_SINGLE, SHADOW_TOP, SHADOW_MIDDLE, SHADOW_BOTTOM, SHADOW_ALL)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ShadowStyle
    }

    var cornerRadius = dp(4)

    @ShadowStyle
    var shadowStyle = SHADOW_SINGLE
    var shadowColor = Color.GRAY
    var shadowRadius = cornerRadius
    var shadowAlpha = 255

    var fillColor = Color.TRANSPARENT

    var offsetTop = dp(0)
    var offsetBottom = dp(0)

    init {
        val styledAttrs = context.theme.obtainStyledAttributes(
            attrs, R.styleable.ShadowView, 0, 0
        )

        with(styledAttrs) {
            cornerRadius = getDimension(R.styleable.ShadowView_cornerRadius, cornerRadius)

            shadowStyle = getInt(R.styleable.ShadowView_style, SHADOW_SINGLE)
            shadowColor = getColor(R.styleable.ShadowView_shadowColor, shadowColor)
            shadowRadius = getDimension(R.styleable.ShadowView_shadowRadius, shadowRadius)
            shadowAlpha = getInt(R.styleable.ShadowView_shadowAlpha, shadowAlpha).coerceIn(0, 255)

            fillColor = getColor(R.styleable.ShadowView_fillColor, fillColor)

            offsetTop = getDimension(R.styleable.ShadowView_offsetTop, offsetTop)
            offsetBottom = getDimension(R.styleable.ShadowView_offsetBottom, offsetBottom)

            recycle()
        }
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        maskFilter = BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.OUTER)
    }

    private val fillPaint = Paint()

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val h = height.toFloat()
        val w = width.toFloat()
        val cR = cornerRadius

        path.reset()

        when (shadowStyle) {
            SHADOW_TOP -> {
                path.addRoundRect(cR, offsetTop + cR, w - cR, h, cR, cR, Path.Direction.CCW)
                path.addRect(cR, offsetTop + cR, w - 2 * cR, h, Path.Direction.CCW)
                path.addRect(cR, offsetTop + cR + cR, w - cR, h, Path.Direction.CCW)
            }
            SHADOW_MIDDLE -> {
                path.addRect(cR, 0f, w - cR, h, Path.Direction.CCW)
            }
            SHADOW_BOTTOM -> {
                path.addRoundRect(cR, cR, w - cR, h - cR - offsetBottom, cR, cR, Path.Direction.CCW)
                path.addRect(cR, 0f, w - cR, h - 2 * cR - offsetBottom, Path.Direction.CCW)
            }
            SHADOW_SINGLE -> {
                path.addRoundRect(cR, cR, w - cR, h - cR, cR, cR, Path.Direction.CCW)
                path.addRect(cR, cR, w - 2 * cR, h - 2 * cR, Path.Direction.CCW)
            }
            SHADOW_ALL -> {
                path.addRoundRect(cR, offsetTop + cR, w - cR, h, cR, cR, Path.Direction.CCW)
            }
        }

        paint.apply {
            color = shadowColor
            alpha = shadowAlpha
        }
        canvas.drawPath(path, paint)

        fillPaint.apply {
            color = fillColor
        }
        canvas.drawPath(path, fillPaint)
    }
}

