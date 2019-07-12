package fuel.hunter

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.Gravity
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
            1 -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.layout_price_item, parent, false)

                PriceItemHolder(view)
            }
            2 -> {
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

class BackgroundItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
//        val shadow = ViewUtils.generateBackgroundWithShadow(
//            view,
//            android.R.color.white,
//            24,
//            R.color.colorHighlight,
//            6,
//            Gravity.CENTER
//            )
//        view.background = shadow
////        view.outlineProvider = object : ViewOutlineProvider() {
//            override fun getOutline(view: View, outline: Outline) {
//                outline.setRoundRect(0, 0, view.width, view.height, 0f)
//            }
//        }
    }
}