package fuel.hunter.view.decorations

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.data.Item
import fuel.hunter.data.header
import fuel.hunter.data.middle

class Separator(
    private val height: Int = 2,
    private val margin: Int = 11,
    private val data: List<Item>
) : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        color = Color.rgb(215, 221, 232)
    }

    private val bounds = Rect()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        parent.children.forEachIndexed { index, view ->
            val type = data[index].typeId

            if (type == header || type == middle) {
                parent.getDecoratedBoundsWithMargins(view, bounds)

                val top = bounds.bottom - height
                val left = bounds.left + margin
                val right = bounds.right - margin
                val rect = Rect(left, top, right, bounds.bottom)

                c.drawRect(rect, paint)
            }
        }
    }
}