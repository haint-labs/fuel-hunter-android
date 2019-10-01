package fuel.hunter.scenes.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat.generateViewId
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.tools.ui.wrapInShadow
import fuel.hunter.view.shadow.ShadowView
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_BOTTOM
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_SINGLE
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

typealias ViewTypeDetector = (index: Int, total: Int) -> Int
typealias ViewLayoutProvider = (viewType: Int) -> Int
typealias ViewHolderBinder<T> = (View, T) -> Unit

val defaultTypeDetector: ViewTypeDetector = { index, total ->
    when (index) {
        0 -> ShadowView.SHADOW_TOP
        total -> ShadowView.SHADOW_BOTTOM
        else -> ShadowView.SHADOW_MIDDLE
    }
}

class BaseViewHolder<T>(
    private val view: View,
    private val binder: ViewHolderBinder<T>
) : RecyclerView.ViewHolder(view) {
    internal fun bind(item: T): Unit = binder(view, item)
}

class BaseListAdapter<T>(
    private val items: List<T>,
    private val layoutProvider: ViewLayoutProvider,
    private val binder: ViewHolderBinder<T>,
    private val viewTypeDetector: ViewTypeDetector = defaultTypeDetector
) : RecyclerView.Adapter<BaseViewHolder<T>>() {
    private val _onItemClick = BroadcastChannel<T>(1)
    val onItemClick: Flow<T> get() = _onItemClick.asFlow()

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int): Int = viewTypeDetector(position, items.size)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val layoutId = layoutProvider(viewType)

        var view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
            .apply { id = generateViewId() }

        if (viewType in SHADOW_SINGLE..SHADOW_BOTTOM) {
            view = wrapInShadow(parent.context, view, viewType)
        }

        return BaseViewHolder(view, binder)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = items[position]

        holder.bind(item)
        holder.itemView.setOnClickListener {
            _onItemClick.offer(item)
        }
    }
}