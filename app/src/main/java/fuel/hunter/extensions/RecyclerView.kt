package fuel.hunter.extensions

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TypedItem<T, I>(
    val type: T,
    val item: I
)

fun RecyclerView.getItemViewType(view: View): Int {
    val index = getChildViewHolder(view).adapterPosition
    return adapter?.getItemViewType(index) ?: 0
}
