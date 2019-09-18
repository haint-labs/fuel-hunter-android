package fuel.hunter.scenes.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.R
import fuel.hunter.databinding.FragmentSettingsBinding
import fuel.hunter.extensions.color
import fuel.hunter.extensions.dp
import fuel.hunter.view.decorations.SeparatorItemDecoration
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentSettingsBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(view) {
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        settingsList.apply {
            layoutManager = LinearLayoutManager(activity)

            addItemDecoration(
                SeparatorItemDecoration(
                    color = color(R.color.itemSeparator),
                    height = dp(1),
                    margin = dp(8),
                    predicate = { false }
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
