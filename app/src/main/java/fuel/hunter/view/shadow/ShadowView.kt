package fuel.hunter.view.shadow

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import fuel.hunter.R
import fuel.hunter.extensions.dp

class ShadowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val SINGLE = 0
    private val TOP = 1
    private val MIDDLE = 2
    private val BOTTOM = 3

    var style = SINGLE
    var color = Color.GRAY
    var cornerRadius = dp(4)
    var shadowRadius = cornerRadius
    var shadowAlpha = 255

    init {
        val attributes = context
            .theme
            .obtainStyledAttributes(attrs, R.styleable.ShadowView, 0, 0)

        style = attributes.getInt(R.styleable.ShadowView_style, SINGLE)
        color = attributes.getColor(R.styleable.ShadowView_shadowColor, color)
        cornerRadius = attributes.getDimension(R.styleable.ShadowView_cornerRadius, cornerRadius)
        shadowRadius = attributes.getDimension(R.styleable.ShadowView_shadowRadius, shadowRadius)
        shadowAlpha = attributes.getInt(R.styleable.ShadowView_shadowAlpha, shadowAlpha).coerceIn(0, 255)

        attributes.recycle()
    }

    private val paint = Paint().also {
        it.color = color
        it.alpha = shadowAlpha
        it.isAntiAlias = true
        it.maskFilter = BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.OUTER)
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val h = height.toFloat()
        val w = width.toFloat()
        val cR = cornerRadius

        path.reset()

        when (style) {
            TOP -> {
                path.addRoundRect(cR, cR, w - cR, h, cR, cR, Path.Direction.CCW)
                path.addRect(cR, cR, w - 2 * cR, h, Path.Direction.CCW)
                path.addRect(cR, cR + cR, w - cR, h, Path.Direction.CCW)
            }
            MIDDLE -> {
                path.addRect(cR, 0f, w - cR, h, Path.Direction.CCW)
            }
            BOTTOM -> {
                path.addRoundRect(cR, cR, w - cR, h - cR, cR, cR, Path.Direction.CCW)
                path.addRect(cR, 0f, w - cR, h - 2 * cR, Path.Direction.CCW)
            }
            SINGLE -> {
                path.addRoundRect(cR, cR, w - cR, h - cR, cR, cR, Path.Direction.CCW)
                path.addRect(cR, cR, w - 2 * cR, h - 2 * cR, Path.Direction.CCW)
            }
        }

        canvas.drawPath(path, paint)
    }
}

