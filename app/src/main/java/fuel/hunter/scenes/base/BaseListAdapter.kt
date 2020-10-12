package fuel.hunter.scenes.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat.generateViewId
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.tools.ui.wrapInShadow
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_BOTTOM
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_SINGLE
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

typealias ViewLayoutProvider = (viewType: Int) -> Int
typealias ViewHolderBinder<T> = (View, T) -> Unit

class BaseViewHolder<T>(
    private val view: View,
    private val binder: ViewHolderBinder<T>
) : RecyclerView.ViewHolder(view) {
    internal fun bind(item: T): Unit = binder(view, item)
}

class BaseListAdapter<T>(
    itemDiff: DiffUtil.ItemCallback<T>,
    private val layoutProvider: ViewLayoutProvider,
    private val binder: ViewHolderBinder<T>,
    private val viewTypeDetector: ViewTypeDetector = ViewTypeDetectors.Default
) : ListAdapter<T, BaseViewHolder<T>>(itemDiff) {
    private val _onItemClick = BroadcastChannel<T>(1)
    val onItemClick: Flow<T> get() = _onItemClick.asFlow()

    override fun getItemViewType(position: Int): Int = viewTypeDetector(position, currentList.size)

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
        val item = currentList[position]

        holder.bind(item)
        holder.itemView.setOnClickListener {
            _onItemClick.offer(item)
        }
    }
}