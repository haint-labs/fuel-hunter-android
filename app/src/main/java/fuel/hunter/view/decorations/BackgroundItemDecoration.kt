package fuel.hunter.view.decorations

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.CustomView
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
            header -> CustomView.Style.TOP
            middle -> CustomView.Style.MIDDLE
            footer -> CustomView.Style.BOTTOM
            single -> CustomView.Style.SINGLE
            else -> CustomView.Style.SINGLE
        }

        val target = view.findViewById<CustomView?>(R.id.shadow)
        target?.let {
            Log.d("ITEM", "yay")
            it.style = style
        }
    }
}