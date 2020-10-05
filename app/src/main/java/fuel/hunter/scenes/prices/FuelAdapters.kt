package fuel.hunter.scenes.prices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import fuel.hunter.R
import fuel.hunter.data.Fuel
import fuel.hunter.extensions.TypedItem
import fuel.hunter.tools.ui.wrapInShadow
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_BOTTOM
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_MIDDLE
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_SINGLE
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_TOP
import kotlinx.android.synthetic.main.layout_price_category.view.*
import kotlinx.android.synthetic.main.layout_price_item.view.*

internal typealias FuelTypedItem = TypedItem<Int, Fuel>

internal const val ITEM_TYPE_CATEGORY = -1
internal val separableItemTypes = listOf(SHADOW_TOP, SHADOW_MIDDLE)

private val wrappables = arrayOf(SHADOW_TOP, SHADOW_MIDDLE, SHADOW_BOTTOM, SHADOW_SINGLE)

fun flattenFuelTypes(
    data: Map<Fuel.Category, List<Fuel.Price>>
): List<FuelTypedItem> {
    return data.entries.flatMap { (category, prices) ->
        val result = mutableListOf(
            FuelTypedItem(ITEM_TYPE_CATEGORY, category)
        )

        if (prices.size == 1) {
            return@flatMap result + FuelTypedItem(SHADOW_SINGLE, prices.first())
        }

        return@flatMap result + prices.mapIndexed { index, item ->
            val type = when (index) {
                0 -> SHADOW_TOP
                prices.lastIndex -> SHADOW_BOTTOM
                else -> SHADOW_MIDDLE
            }

            FuelTypedItem(type, item)
        }
    }
}

internal val fuelTypedItemDiff = object : DiffUtil.ItemCallback<FuelTypedItem>() {
    override fun areItemsTheSame(oldItem: FuelTypedItem, newItem: FuelTypedItem): Boolean {
        return oldItem.type == newItem.type && oldItem.item == newItem.item
    }

    override fun areContentsTheSame(oldItem: FuelTypedItem, newItem: FuelTypedItem): Boolean {
        return oldItem.type == newItem.type
                && oldItem.item == newItem.item
    }
}

class PricesAdapter : ListAdapter<FuelTypedItem, PricesViewHolder>(fuelTypedItemDiff) {
    override fun getItemViewType(position: Int) = currentList[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PricesViewHolder {
        val isItem = viewType in wrappables
        val layout =
            if (isItem) R.layout.layout_price_item else R.layout.layout_price_category

        var view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
            .apply {
                if (id == View.NO_ID) {
                    id = View.generateViewId()
                }
            }

        if (isItem) view = wrapInShadow(parent.context, view, viewType)

        return PricesViewHolder(view)
    }

    override fun onBindViewHolder(holder: PricesViewHolder, position: Int) {
        holder.bind(currentList[position].item)
    }
}

class PricesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Fuel) {
        when (item) {
            is Fuel.Price -> {
                view.title.text = item.title
                view.text.text = item.address
                view.accent.text = item.price.toString()
                when (item.logo) {
                    is Fuel.Logo.Url -> view.icon.load(item.logo.url)
                    is Fuel.Logo.Drawable -> view.icon.load(item.logo.id)
                }
            }
            is Fuel.Category -> {
                view.header.text = item.name
            }
        }
    }
}