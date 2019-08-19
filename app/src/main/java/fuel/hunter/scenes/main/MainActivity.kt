package fuel.hunter.scenes.main

import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.R
import fuel.hunter.data.FuelCategory
import fuel.hunter.data.FuelPrice
import fuel.hunter.data.Item
import fuel.hunter.extensions.*
import fuel.hunter.router.Screen
import fuel.hunter.router.Router
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : AppCompatActivity(), Router {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = resources.getColor(android.R.color.white, null)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(false)
            setDisplayShowTitleEnabled(false)
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.root, FuelPriceListFragment.create(this))
                .commit()
        }
    }

    override fun goTo(screen: Screen) {
        val fragment = when (screen) {
            is Screen.Savings -> SavingsFragment.create(this)
            is Screen.Main -> FuelPriceListFragment.create(this)
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.root, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupTransition() {
        val slide = Slide(Gravity.START).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
        }

        with(window) {
            exitTransition = slide
            enterTransition = slide
        }
    }
}

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

        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutResId, parent, false)

        return PriceItemHolder(view)
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
