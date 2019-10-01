package fuel.hunter.scenes.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.R
import fuel.hunter.extensions.color
import fuel.hunter.extensions.dp
import fuel.hunter.tools.navigateUp
import fuel.hunter.view.decorations.SeparatorItemDecoration
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_MIDDLE
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_TOP
import kotlinx.android.synthetic.main.fragment_base_list.*
import kotlinx.coroutines.channels.BroadcastChannel

abstract class BaseFragment<T> : Fragment() {
    private val _channel = BroadcastChannel<T>(1)

    abstract val title: Int
    abstract val items: List<T>
    abstract val layoutProvider: ViewLayoutProvider
    abstract val binder: ViewHolderBinder<T>

    open var navIcon: Int = R.drawable.ic_back_arrow
    open var viewTypeDetector: ViewTypeDetector = ViewTypeDetectors.Default

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

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                toolbarShadow.alpha = with(recyclerView) {
                    val offset = computeVerticalScrollOffset()
                    val max = dp(50)

                    offset.coerceIn(0, max.toInt()) / max
                }
            }
        })
    }
}
