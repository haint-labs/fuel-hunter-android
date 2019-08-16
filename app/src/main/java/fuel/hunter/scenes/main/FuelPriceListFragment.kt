package fuel.hunter.scenes.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.R
import fuel.hunter.data.dummyData
import fuel.hunter.extensions.color
import fuel.hunter.extensions.dp
import fuel.hunter.view.decorations.SeparatorItemDecoration
import kotlinx.android.synthetic.main.fragment_main_fuel_prices.*
import kotlinx.android.synthetic.main.layout_notes.*
import kotlinx.android.synthetic.main.layout_toolbar_shadow.*

class FuelPriceListFragment : Fragment() {

    companion object {
        fun create() = FuelPriceListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main_fuel_prices, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupPriceList()
    }

    private fun setupPriceList() {
        priceList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = PriceListAdapter(dummyData)

            addItemDecoration(
                SeparatorItemDecoration(
                    color = color(R.color.itemSeparator),
                    height = dp(1),
                    margin = dp(8)
                )
            )
        }

        priceList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val alpha = with(recyclerView) {
                    val offset = computeVerticalScrollOffset()
                    val max = dp(50)

                    offset.coerceIn(0, max.toInt()) / max
                }

                // TODO: that's bad
                activity?.toolbarShadow?.alpha = alpha
            }
        })

        priceList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val alpha = with(recyclerView) {
                    val offset = computeVerticalScrollOffset()
                    val extent = computeVerticalScrollExtent()
                    val range = computeVerticalScrollRange()

                    val max = dp(50)

                    (range - extent - offset).coerceIn(0, max.toInt()) / max
                }

                notesShadow.alpha = alpha
            }
        })
    }
}