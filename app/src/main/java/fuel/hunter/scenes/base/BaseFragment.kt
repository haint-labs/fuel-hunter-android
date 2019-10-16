package fuel.hunter.scenes.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import fuel.hunter.R
import fuel.hunter.extensions.color
import fuel.hunter.extensions.dp
import fuel.hunter.extensions.onScroll
import fuel.hunter.tools.navigateUp
import fuel.hunter.view.decorations.SeparatorItemDecoration
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_MIDDLE
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_TOP
import kotlinx.android.synthetic.main.fragment_base_list.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<T> : Fragment() {
    abstract val title: Int
    abstract val items: List<T>
    abstract val layoutProvider: ViewLayoutProvider
    abstract val binder: ViewHolderBinder<T>

    open var navIcon: Int = R.drawable.ic_back_arrow
    open var viewTypeDetector = ViewTypeDetectors.Default

    val adapter by lazy { BaseListAdapter(items, layoutProvider, binder, viewTypeDetector) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_base_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupList()
    }

    private fun setupToolbar() = toolbar.apply {
        toolbarTitle.text = getString(this@BaseFragment.title)
        setNavigationIcon(navIcon)
        setNavigationOnClickListener { navigateUp() }
    }

    private fun setupList() = listView.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = this@BaseFragment.adapter

        addItemDecoration(
            SeparatorItemDecoration(
                color = color(R.color.itemSeparator),
                height = dp(1),
                margin = dp(8),
                predicate = { it == SHADOW_TOP || it == SHADOW_MIDDLE }
            )
        )

        onScroll
            .onEach {
                toolbarShadow.alpha = with(listView) {
                    val offset = computeVerticalScrollOffset()
                    val max = dp(50)

                    offset.coerceIn(0, max.toInt()) / max
                }
            }
            .launchIn(lifecycleScope)
    }
}
