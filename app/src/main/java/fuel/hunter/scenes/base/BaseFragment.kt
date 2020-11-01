package fuel.hunter.scenes.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import fuel.hunter.R
import fuel.hunter.databinding.FragmentBaseListBinding
import fuel.hunter.extensions.*
import fuel.hunter.tools.navigateUp
import fuel.hunter.view.decorations.SeparatorItemDecoration
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_MIDDLE
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_TOP
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<T> : Fragment() {
    abstract val title: Int
    abstract val itemDiff: DiffUtil.ItemCallback<T>
    abstract val layoutProvider: ViewLayoutProvider
    abstract val binder: ViewHolderBinder<T>

    open var navIcon: Int = R.drawable.ic_back_arrow
    open var viewTypeDetector = ViewTypeDetectors.Default

    val adapter by lazy { BaseListAdapter(itemDiff, layoutProvider, binder, viewTypeDetector) }

    private val binding by viewBinding(FragmentBaseListBinding::bind)

    private val toolbar get() = binding.toolbar
    private val toolbarTitle get() = binding.toolbarTitle
    private val toolbarShadow get() = binding.toolbarShadow
    private val listView get() = binding.listView

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
