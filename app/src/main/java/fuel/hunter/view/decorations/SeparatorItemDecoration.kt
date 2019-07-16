package fuel.hunter.view.decorations

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import androidx.annotation.ColorInt
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.extensions.ITEM_TYPE_HEADER
import fuel.hunter.extensions.ITEM_TYPE_MIDDLE
import fuel.hunter.extensions.getItemViewType

class SeparatorItemDecoration(
    @ColorInt color: Int,
    private val height: Float,
    private val margin: Float
) : RecyclerView.ItemDecoration() {

    private val paint = Paint().also {
        it.color = color
    }

    private val bounds = Rect()

    override fun onDraw(
        canvas: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) = parent.children.forEachIndexed { index, view ->
        when (parent.getItemViewType(index)) {
            ITEM_TYPE_HEADER, ITEM_TYPE_MIDDLE -> {
                parent.getDecoratedBoundsWithMargins(view, bounds)

                val top = bounds.bottom - height
                val left = bounds.left + margin
                val right = bounds.right - margin
                val rect = RectF(left, top, right, bounds.bottom.toFloat())

                canvas.drawRect(rect, paint)
            }
        }
    }
}