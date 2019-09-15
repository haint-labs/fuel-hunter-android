package fuel.hunter.scenes.prices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.R
import fuel.hunter.data.Fuel
import fuel.hunter.extensions.TypedItem
import fuel.hunter.tools.mapper

internal const val ITEM_TYPE_SINGLE = 0
internal const val ITEM_TYPE_HEADER = 1
internal const val ITEM_TYPE_MIDDLE = 2
internal const val ITEM_TYPE_FOOTER = 3
internal const val ITEM_TYPE_CATEGORY = 4

internal val separableItemTypes = listOf(ITEM_TYPE_HEADER, ITEM_TYPE_MIDDLE)

internal typealias FuelTypedItem = TypedItem<Int, Fuel>

internal fun flattenFuelTypes(data: Map<Fuel.Category, List<Fuel.Price>>): List<FuelTypedItem> {
    return data.entries.flatMap { (category, prices) ->
        val result = mutableListOf(
            FuelTypedItem(ITEM_TYPE_CATEGORY, category)
        )

        if (prices.size == 1) {
            return@flatMap result + FuelTypedItem(ITEM_TYPE_SINGLE, prices.first())
        }

        return@flatMap result + prices.mapIndexed { index, item ->
            val type = when (index) {
                0 -> ITEM_TYPE_HEADER
                prices.lastIndex -> ITEM_TYPE_FOOTER
                else -> ITEM_TYPE_MIDDLE
            }

            FuelTypedItem(type, item)
        }
    }
}

class PricesAdapter(
    private val items: List<FuelTypedItem>
) : RecyclerView.Adapter<PricesViewHolder>() {

    private val itemTypeLayoutMapper = mapper(
        ITEM_TYPE_HEADER to R.layout.layout_price_item_top,
        ITEM_TYPE_MIDDLE to R.layout.layout_price_item_middle,
        ITEM_TYPE_FOOTER to R.layout.layout_price_item_bottom,
        ITEM_TYPE_SINGLE to R.layout.layout_price_item_single,
        ITEM_TYPE_CATEGORY to R.layout.layout_price_category
    )

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PricesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(itemTypeLayoutMapper.valueFor(viewType), parent, false)

        return PricesViewHolder(view)
    }

    override fun onBindViewHolder(holder: PricesViewHolder, position: Int) {
        holder.bind(items[position].item)
    }
}

class PricesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Fuel) {
        when (item) {
            is Fuel.Price -> {
                view.findViewById<TextView>(R.id.title).text = item.title
                view.findViewById<TextView>(R.id.address).text = item.address
                view.findViewById<TextView>(R.id.price).text = item.price.toString()
                view.findViewById<ImageView>(R.id.icon).setImageResource(item.logo)
            }
            is Fuel.Category -> {
                view.findViewById<TextView>(R.id.header).text = item.name
            }
        }
    }
}