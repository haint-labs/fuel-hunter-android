package fuel.hunter.tools.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.plusAssign
import fuel.hunter.R
import fuel.hunter.view.shadow.ShadowView
import fuel.hunter.view.shadow.ShadowView.Companion.ShadowStyle

fun wrapInShadow(
    context: Context,
    item: View,
    @ShadowStyle shadowStyle: Int
): ConstraintLayout {
    val shadowView = ShadowView(context).apply {
        id = View.generateViewId()
        layoutParams = ViewGroup.LayoutParams(0, 0)

        this.shadowStyle = shadowStyle
        cornerRadius = resources.getDimension(R.dimen.listItemRadius)
        shadowRadius = resources.getDimension(R.dimen.listItemShadowRadius)
        shadowColor = ResourcesCompat.getColor(
            resources,
            R.color.itemShadow,
            context.theme
        )
        shadowAlpha = 102
    }

    val container = ConstraintLayout(context).apply {
        id = View.generateViewId()
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    container += shadowView
    container += item

    ConstraintSet()
        .also { it.clone(container) }
        .apply {
            with(shadowView) {
                connect(
                    id,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START
                )
                connect(
                    id,
                    ConstraintSet.END,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.END
                )
                connect(
                    id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP
                )
                connect(
                    id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
            }

            with(item) {
                val margin = resources.getDimensionPixelSize(R.dimen.listItemMargin)
                val radius = resources.getDimensionPixelSize(R.dimen.listItemRadius)

                val (start, top, end, bottom) = when (shadowStyle) {
                    ShadowView.SHADOW_TOP -> arrayOf(margin, margin, margin, radius)
                    ShadowView.SHADOW_MIDDLE -> arrayOf(margin, radius, margin, radius)
                    ShadowView.SHADOW_BOTTOM -> arrayOf(margin, radius, margin, margin)
                    ShadowView.SHADOW_SINGLE -> arrayOf(margin, margin, margin, margin)
                    else -> throw IllegalStateException("Unknown shadow shadowStyle")
                }

                connect(
                    id,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START, start
                )
                connect(
                    id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP, top
                )
                connect(
                    id,
                    ConstraintSet.END,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.END, end
                )
                connect(
                    id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM, bottom
                )
            }
        }
        .applyTo(container)

    return container
}