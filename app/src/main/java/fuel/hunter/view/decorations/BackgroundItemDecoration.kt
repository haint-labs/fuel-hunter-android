package fuel.hunter.view.decorations

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.view.shadow.ShadowView
import fuel.hunter.R
import fuel.hunter.data.*

class BackgroundItemDecoration(private val data: List<Item>) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val offset = parent.getChildAdapterPosition(view)

        val item = data[offset]

        val style = when (item.typeId) {
            header -> ShadowView.Style.TOP
            middle -> ShadowView.Style.MIDDLE
            footer -> ShadowView.Style.BOTTOM
            single -> ShadowView.Style.SINGLE
            else -> ShadowView.Style.SINGLE
        }

        val target = view.findViewById<ShadowView?>(R.id.shadow)
        target?.let {
            Log.d("ITEM", "yay")
            it.style = style
        }
    }
}