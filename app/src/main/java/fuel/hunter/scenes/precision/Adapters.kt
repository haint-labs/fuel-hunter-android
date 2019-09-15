package fuel.hunter.scenes.precision

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.R
import fuel.hunter.extensions.TypedItem
import fuel.hunter.tools.mapper
import kotlinx.android.synthetic.main.layout_price_item_content.view.*

internal const val ITEM_TYPE_TEXT = 0
internal const val ITEM_TYPE_HEADER = 1
internal const val ITEM_TYPE_MIDDLE = 2
internal const val ITEM_TYPE_FOOTER = 3

internal val separableItemTypes = listOf(
    ITEM_TYPE_HEADER,
    ITEM_TYPE_MIDDLE
)

internal sealed class PrecisionInfo {
    object Summary : PrecisionInfo()

    class FuelProvider(
        val logo: Int,
        val name: String,
        val description: String
    ) : PrecisionInfo()
}

internal typealias PrecisionTypedItem = TypedItem<Int, PrecisionInfo>

internal class PrecisionInfoAdapter(
    private val items: List<PrecisionTypedItem>
) : RecyclerView.Adapter<PrecisionInfoViewHolder>() {

    private val itemTypeLayoutMapper = mapper(
        ITEM_TYPE_TEXT to R.layout.layout_precision_disclaimer,
        ITEM_TYPE_HEADER to R.layout.layout_price_item_top,
        ITEM_TYPE_MIDDLE to R.layout.layout_price_item_middle,
        ITEM_TYPE_FOOTER to R.layout.layout_price_item_bottom
    )

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrecisionInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(itemTypeLayoutMapper.valueFor(viewType), parent, false)

        return PrecisionInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrecisionInfoViewHolder, position: Int) {
        holder.bind(items[position].item)
    }
}

internal class PrecisionInfoViewHolder(private val view: View) :
    RecyclerView.ViewHolder(view) {
    fun bind(item: PrecisionInfo) {
        when (item) {
            is PrecisionInfo.FuelProvider -> {
                view.icon.setImageResource(item.logo)
                view.title.text = item.name
                view.text.text = item.description
                view.accent.visibility = View.GONE
            }
        }
    }
}