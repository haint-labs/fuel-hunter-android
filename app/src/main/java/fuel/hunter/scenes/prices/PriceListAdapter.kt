package fuel.hunter.scenes.prices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.R
import fuel.hunter.data.Item
import fuel.hunter.extensions.*

class PriceListAdapter(
    private val items: List<Item>
) : RecyclerView.Adapter<PriceItemHolder>() {
    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = items[position].typeId

    private val layoutMap = mapOf(
        ITEM_TYPE_HEADER to R.layout.layout_price_item_top,
        ITEM_TYPE_MIDDLE to R.layout.layout_price_item_middle,
        ITEM_TYPE_FOOTER to R.layout.layout_price_item_bottom,
        ITEM_TYPE_SINGLE to R.layout.layout_price_item_single,
        ITEM_TYPE_CATEGORY to R.layout.layout_price_category
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceItemHolder {
        val layoutResId = checkNotNull(layoutMap[viewType])

        val view = LayoutInflater.from(parent.context)
            .inflate(layoutResId, parent, false)

        return PriceItemHolder(view)
    }

    override fun onBindViewHolder(holder: PriceItemHolder, position: Int) {
        holder.bind(items[position])
    }

}