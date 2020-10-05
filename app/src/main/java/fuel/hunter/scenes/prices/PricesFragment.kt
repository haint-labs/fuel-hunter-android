package fuel.hunter.scenes.prices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import fuel.hunter.MainViewModel
import fuel.hunter.R
import fuel.hunter.databinding.FragmentPricesBinding
import fuel.hunter.extensions.color
import fuel.hunter.extensions.dp
import fuel.hunter.extensions.onScroll
import fuel.hunter.tools.navigateTo
import fuel.hunter.view.decorations.SeparatorItemDecoration
import kotlinx.android.synthetic.main.fragment_prices.*
import kotlinx.android.synthetic.main.layout_notes.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PricesFragment : Fragment() {
    private val viewModel by activityViewModels<MainViewModel>()

    private val adapter by lazy { PricesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentPricesBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupPriceList()
        handleSavingsTap()

        viewModel.prices
            .observe(viewLifecycleOwner, adapter::submitList)
    }

    private fun setupPriceList() {
        priceList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@PricesFragment.adapter

            addItemDecoration(
                SeparatorItemDecoration(
                    color = color(R.color.itemSeparator),
                    height = dp(1),
                    margin = dp(8),
                    predicate = { separableItemTypes.contains(it) }
                )
            )

            onScroll
                .onEach { (recyclerView, _, _) ->
                    val alpha = with(recyclerView) {
                        val offset = computeVerticalScrollOffset()
                        val max = dp(50)

                        offset.coerceIn(0, max.toInt()) / max
                    }

                    // TODO: that's bad
                    activity?.toolbarShadow?.alpha = alpha
                }
                .launchIn(lifecycleScope)

            onScroll
                .onEach { (recyclerView, _, _) ->
                    val alpha = with(recyclerView) {
                        val offset = computeVerticalScrollOffset()
                        val extent = computeVerticalScrollExtent()
                        val range = computeVerticalScrollRange()

                        val max = dp(50)

                        (range - extent - offset).coerceIn(0, max.toInt()) / max
                    }

                    notesShadow.alpha = alpha
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun handleSavingsTap() {
        goToSavings.setOnClickListener { navigateTo(R.id.main_to_savings) }
        goToPrecision.setOnClickListener { navigateTo(R.id.main_to_precision) }
        toolbar.setNavigationOnClickListener { navigateTo(R.id.main_to_settings) }
    }
}