package fuel.hunter.scenes.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.R
import fuel.hunter.extensions.color
import fuel.hunter.extensions.dp
import fuel.hunter.view.decorations.SeparatorItemDecoration
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_MIDDLE
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_TOP
import kotlinx.android.synthetic.main.fragment_base_list.*

abstract class BaseFragment<T> : Fragment() {

    abstract val title: Int
    var navIcon: Int = R.drawable.ic_back_arrow

    abstract val items: List<T>

    abstract val binder: ViewHolderBinder<T>
    abstract val layoutProvider: ViewLayoutProvider

    var viewTypeDetector: ViewTypeDetector = defaultTypeDetector

    abstract val onClick: ItemClickListener<T>

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
        setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupList() = listView.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = BaseListAdapter(items, layoutProvider, binder, viewTypeDetector, onClick)

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
