package fuel.hunter.view.decorations

import android.graphics.Canvas
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.R
import fuel.hunter.extensions.ITEM_TYPE_FOOTER
import fuel.hunter.extensions.ITEM_TYPE_HEADER
import fuel.hunter.extensions.ITEM_TYPE_MIDDLE
import fuel.hunter.extensions.getItemViewType
import fuel.hunter.view.shadow.ShadowView

class BackgroundItemDecoration : RecyclerView.ItemDecoration() {
    override fun onDraw(
        canvas: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) = parent.children.forEachIndexed { index, view ->
        val style = when (parent.getItemViewType(index)) {
            ITEM_TYPE_HEADER -> ShadowView.Style.TOP
            ITEM_TYPE_MIDDLE -> ShadowView.Style.MIDDLE
            ITEM_TYPE_FOOTER -> ShadowView.Style.BOTTOM
            else -> ShadowView.Style.SINGLE
        }

        val target = view.findViewById<ShadowView?>(R.id.shadow)
        target?.let {
            it.style = style
        }
    }
}