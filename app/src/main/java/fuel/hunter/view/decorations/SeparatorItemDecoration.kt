package fuel.hunter.view.decorations

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import androidx.annotation.ColorInt
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.extensions.getItemViewType

class SeparatorItemDecoration(
    @ColorInt color: Int,
    private val height: Float,
    private val margin: Float,
    private val predicate: (Int) -> Boolean
) : RecyclerView.ItemDecoration() {

    private val bounds = Rect()

    private val paint = Paint().also {
        it.color = color
    }

    override fun onDraw(
        canvas: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        parent.children
            .filter { predicate(parent.getItemViewType(it)) }
            .forEach {
                parent.getDecoratedBoundsWithMargins(it, bounds)

                val top = bounds.bottom - height
                val left = bounds.left + margin
                val right = bounds.right - margin
                val rect = RectF(left, top, right, bounds.bottom.toFloat())

                canvas.drawRect(rect, paint)
            }
    }
}