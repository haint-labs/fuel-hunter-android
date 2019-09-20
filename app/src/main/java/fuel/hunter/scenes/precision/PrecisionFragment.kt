package fuel.hunter.scenes.precision

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.R
import fuel.hunter.databinding.FragmentPrecisionBinding
import fuel.hunter.extensions.color
import fuel.hunter.extensions.dp
import fuel.hunter.view.decorations.SeparatorItemDecoration
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_BOTTOM
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_MIDDLE
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_TOP
import kotlinx.android.synthetic.main.fragment_precision.view.*
import kotlinx.android.synthetic.main.layout_toolbar.*

internal val precisionInfo = listOf(
    PrecisionTypedItem(
        ITEM_TYPE_TEXT,
        PrecisionInfo.Summary
    ),
    PrecisionTypedItem(
        SHADOW_TOP,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_neste,
            "Neste",
            "Lētākā degviela Rīgā."
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_circlek,
            "Circle K",
            "Lētākā degviela Rīgā."
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_kool,
            "Kool",
            "Lētākā degviela Rīgā."
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_ln,
            "Latvijas Nafta",
            "Zemākās cenas DUS tīklā Latvijā, pa reģioniem (Rīgas rajons, Liepājas rajons, Ventspils rajons, Zemgale, Vidzeme, Latgale)"
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_viada,
            "Viada",
            "Patreiz nav pieejamas cenas."
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_virshi,
            "Virši",
            "Lētākā degviela Rīgā un Pierīgā."
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_gotika,
            "Gotika Auto",
            "Zemākās cenas DUS tīklā Latvijā."
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_astarte,
            "ASTARTE",
            "Patreiz nav pieejamas cenas."
        )
    ),
    PrecisionTypedItem(
        SHADOW_BOTTOM,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_dinaz,
            "DINAZ",
            "Patreiz nav pieejamas cenas."
        )
    )
)

class PrecisionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentPrecisionBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(view) {
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        precisionInfoList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = PrecisionInfoAdapter(precisionInfo)

            addItemDecoration(
                SeparatorItemDecoration(
                    color = color(R.color.itemSeparator),
                    height = dp(1),
                    margin = dp(8),
                    predicate = { separableItemTypes.contains(it) }
                )
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        }
    }
}
