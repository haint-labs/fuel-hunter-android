package fuel.hunter.scenes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import fuel.hunter.databinding.FragmentSavingsBinding
import fuel.hunter.extensions.dp
import kotlinx.android.synthetic.main.fragment_savings.view.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class SavingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentSavingsBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        toolbarShadow.alpha = 0f

        scrollView
            .viewTreeObserver
            .addOnScrollChangedListener {
                val max = dp(50)
                val alpha = scrollView.scrollY.coerceIn(0, max.toInt()) / max

                toolbarShadow.alpha = alpha
            }
    }
}
