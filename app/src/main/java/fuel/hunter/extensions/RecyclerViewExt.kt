package fuel.hunter.extensions

import android.view.View
import androidx.recyclerview.widget.RecyclerView

const val ITEM_TYPE_SINGLE = 0
const val ITEM_TYPE_HEADER = 1
const val ITEM_TYPE_MIDDLE = 2
const val ITEM_TYPE_FOOTER = 3
const val ITEM_TYPE_CATEGORY = 4

fun RecyclerView.getItemType(view: View): Int {
    val index = getChildViewHolder(view).adapterPosition
    return adapter?.getItemViewType(index) ?: ITEM_TYPE_SINGLE
}
