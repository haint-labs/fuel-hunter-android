package fuel.hunter

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.data.FuelCategory
import fuel.hunter.data.FuelPrice
import fuel.hunter.data.Item
import fuel.hunter.data.dummyData
import fuel.hunter.extensions.*
import fuel.hunter.view.decorations.BackgroundItemDecoration
import fuel.hunter.view.decorations.SeparatorItemDecoration
import fuel.hunter.view.shadow.dp

class MainActivity : AppCompatActivity() {

    private val priceList by lazy { findViewById<RecyclerView>(R.id.priceList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = resources.getColor(android.R.color.white, null)
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(false)
            setDisplayShowTitleEnabled(false)
        }

        setupPriceList()
    }

    private fun setupPriceList() {
        priceList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = PriceListAdapter(dummyData)

            addItemDecoration(SeparatorItemDecoration(
                color = color(R.color.itemSeparator),
                height = dp(1),
                margin = dp(4)
            ))

            addItemDecoration(BackgroundItemDecoration())
        }
    }
}

class PriceListAdapter(
    private val items: List<Item>
) : RecyclerView.Adapter<PriceItemHolder>() {
    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = items[position].typeId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceItemHolder {
        return when (viewType) {
            ITEM_TYPE_HEADER, ITEM_TYPE_FOOTER, ITEM_TYPE_SINGLE, ITEM_TYPE_MIDDLE -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.layout_price_item, parent, false)

                PriceItemHolder(view)
            }
            ITEM_TYPE_CATEGORY -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.layout_price_category, parent, false)

                PriceItemHolder(view)
            }
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: PriceItemHolder, position: Int) {
        holder.bind(items[position])
    }

}

class PriceItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Item) {
        when (item) {
            is FuelPrice -> {
                view.findViewById<TextView>(R.id.title).text = item.title
                view.findViewById<TextView>(R.id.address).text = item.address
                view.findViewById<TextView>(R.id.price).text = item.price.toString()
                view.findViewById<ImageView>(R.id.icon).setImageResource(item.logo)
            }
            is FuelCategory -> {
                view.findViewById<TextView>(R.id.header).text = item.name
            }
        }
    }
}
