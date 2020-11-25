package fuel.hunter.extensions

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

data class TypedItem<T, I>(
    val type: T,
    val item: I
)

fun RecyclerView.getItemViewType(view: View): Int {
    val index = getChildViewHolder(view)
        .adapterPosition
        .takeUnless { it == RecyclerView.NO_POSITION }
        ?: return 0

    return adapter?.getItemViewType(index) ?: 0
}

val RecyclerView.onScroll: Flow<Triple<RecyclerView, Int, Int>>
    get() = channelFlow {
        val wrapped = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                offer(Triple(recyclerView, dx, dy))
            }
        }

        addOnScrollListener(wrapped)

        awaitClose { removeOnScrollListener(wrapped) }
    }