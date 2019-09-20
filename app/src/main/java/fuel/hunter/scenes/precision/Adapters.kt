package fuel.hunter.scenes.precision

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.R
import fuel.hunter.extensions.TypedItem
import fuel.hunter.tools.ui.wrapInShadow
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_MIDDLE
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_TOP
import kotlinx.android.synthetic.main.layout_price_item.view.*

internal const val ITEM_TYPE_TEXT = -1

internal val separableItemTypes = listOf(SHADOW_TOP, SHADOW_MIDDLE)

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
    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = items[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrecisionInfoViewHolder {
        val isItem = viewType != ITEM_TYPE_TEXT
        val layout =
            if (isItem) R.layout.layout_price_item else R.layout.layout_precision_disclaimer

        var view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
            .apply {
                if (id == View.NO_ID) {
                    id = View.generateViewId()
                }
            }

        if (isItem) view = wrapInShadow(parent.context, view, viewType)

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