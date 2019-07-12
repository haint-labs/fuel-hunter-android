package fuel.hunter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.data.*

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

            addItemDecoration(Separator(data = dummyData))
            addItemDecoration(BackgroundItemDecoration(dummyData))
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
            header, footer, single, middle -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.layout_price_item, parent, false)

                PriceItemHolder(view)
            }
            category -> {
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

class Separator(
    private val height: Int = 2,
    private val margin: Int = 11,
    private val data: List<Item>
) : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        color = Color.rgb(215, 221, 232)
    }

    private val bounds = Rect()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        parent.children.forEachIndexed { index, view ->
            val type = data[index].typeId

            if (type == header || type == middle) {
                parent.getDecoratedBoundsWithMargins(view, bounds)

                val top = bounds.bottom - height
                val left = bounds.left + margin
                val right = bounds.right - margin
                val rect = Rect(left, top, right, bounds.bottom)

                c.drawRect(rect, paint)
            }
        }
    }
}

class BackgroundItemDecoration(private val data: List<Item>) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val offset = parent.getChildAdapterPosition(view)

        val item = data[offset]

        val style = when (item.typeId) {
            header -> CustomView.Style.TOP
            middle -> CustomView.Style.MIDDLE
            footer -> CustomView.Style.BOTTOM
            single -> CustomView.Style.SINGLE
            else -> CustomView.Style.SINGLE
        }

        val target = view.findViewById<CustomView?>(R.id.shadow)
        target?.let {
            Log.d("ITEM", "yay")
            it.style = style
        }
    }
}